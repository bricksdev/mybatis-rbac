/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import cn.com.bricks.mybatis.rbac.cache.FilterCache;
import cn.com.bricks.mybatis.rbac.cache.Table;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rbac权限拦截器
 *
 * @author kete2003@gmail.com
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DynamicRbacInterceptor implements Interceptor {

    protected static Logger log = LoggerFactory.getLogger(DynamicRbacInterceptor.class);
    protected static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    protected static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    protected static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        // 获取定义好的sql
        BoundSql bsql = statementHandler.getBoundSql();
        // 重新组装绑定sql
        TextSqlNode sqlNode = new TextSqlNode(bsql.getSql());
        BoundSql nbsql = getBoundSql(mappedStatement.getConfiguration(), bsql.getParameterObject(), sqlNode);
        // 重新设定sql语句到绑定中
        metaStatementHandler.setValue("delegate.boundSql.sql", nbsql.getSql());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties p) {
    }

    protected BoundSql getBoundSql(Configuration configuration, Object parameterObject, SqlNode sqlNode) throws SQLException {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        sqlNode.apply(context);
        String countextSql = context.getSql();
        // 动态绑定
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        String sql = modifySqlAddUserRole(countextSql, parameterObject);
        SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());

        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }

        return boundSql;
    }

    protected String modifySqlAddUserRole(String sql, Object parameterObject) throws SQLException {
        FilterSql fsql = new FilterSql(sql);
        FilterCache filtercache = ClientRoleFilterService.getFilterData();
        List<String> roles = ClientRoleService.getCurrentUserRoles();
        Map<String, Table> tables = filtercache.getTables(roles);
        for (Table table : tables.values()) {

            if (fsql.containTable(table)) {
                fsql.replace(table, null);
            }
        }
        return fsql.getNewSql();
    }
}
