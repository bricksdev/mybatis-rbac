/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import cn.com.bricks.mybatis.rbac.cache.Obscure;
import cn.com.bricks.mybatis.rbac.cache.ObscureCache;
import cn.com.bricks.mybatis.rbac.cache.ObscureField;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据混淆拦截器
 *
 * @author kete2003@gmail.com
 */
@Intercepts({
    @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class DynamicObscureInterceptor implements Interceptor {
    
    protected static Logger log = LoggerFactory.getLogger(DynamicRbacInterceptor.class);
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        Object obj = invocation.proceed();
        if (obj != null) {
            ObscureCache obscureCache = ClientObscureFilterService.getObscureData();
            Map<String, Obscure> obsurecfg = obscureCache.getObscureConfigs(ClientRoleService.getCurrentUserRoles());
            // 只有存在混淆才进行处理
            if (obsurecfg != null && !obsurecfg.isEmpty()) {
                // 如果是List对象，就
                if (obj instanceof List) {
                    List results = (List) obj;
                    for (Object ret : results) {
                        // TODO 先不处理类型转换问题默认为字符类型才能进行处理
                        // 对于相应的类属性值进行混淆处理
                        if (ret != null) {
                            // 遍历所有权限角色信息 进行模糊处理
                            for (Map.Entry<String, Obscure> entry : obsurecfg.entrySet()) {
                                setObscureValue(ret, entry.getValue());
                            }
                        }
                    }
                }
            }
        }
        return obj;
    }

    /**
     * 处理混淆
     *
     * @param obj
     * @param obscure
     */
    protected void setObscureValue(Object obj, Obscure obscure) {
        if (obj == null || obscure == null) {
            return;
        }
        
        List<ObscureField> ofields = obscure.getFields();
        if (ofields != null) {
            for (ObscureField fld : ofields) {
                if (!obj.getClass().getName().equals(fld.getClassName())) {
                    continue;
                }
                List<String> fields = fld.getFields();
                if (fields != null && !fields.isEmpty()) {
                    for (String fild : fields) {
                        
                        try {
                            String value = BeanUtils.getProperty(obj, fild);
                            // 为空不处理
                            if (value == null) {
                                continue;
                            }
                            String replaceValue = obscure.getObscureValue();
                            // 0 为中间2/3部分被混淆内容替换
                            if ("0".equals(obscure.getObscureType())) {
                                int length = value.length();
                                if (length >= 3) {
                                    // 获取1/3的前索引
                                    int frontIdx = (int) length / 3;
                                    // 获取1/3的后索引
                                    int lastIdx = (int) length * 2 / 3;
                                    // 替换需要替换部分
                                    replaceValue = value.replaceFirst(value.substring(frontIdx, lastIdx), replaceValue);
                                }
                                BeanUtils.setProperty(obj, fild, replaceValue);
                                // 1 为全部被混淆内容替换
                            } else if ("1".equals(obscure.getObscureType())) {
                                BeanUtils.setProperty(obj, fild, replaceValue);
                                // 2 为前2/3部分被混淆内容替换
                            } else if ("2".equals(obscure.getObscureType())) {
                                int length = value.length();
                                if (length >= 3) {
                                    // 获取1/3的前索引
                                    int frontIdx = 0;
                                    // 获取1/3的后索引
                                    int lastIdx = (int) length * 2 / 3;
                                    // 替换需要替换部分
                                    replaceValue = value.replaceFirst(value.substring(frontIdx, lastIdx), replaceValue);
                                }
                                BeanUtils.setProperty(obj, fild, replaceValue);
                                // 3 为后2/3部分被混淆内容替换
                            } else if ("3".equals(obscure.getObscureType())) {
                                int length = value.length();
                                if (length >= 3) {
                                    // 获取1/3的前索引
                                    int frontIdx = (int) length / 3;
                                    // 获取1/3的后索引
                                    int lastIdx = length;
                                    // 替换需要替换部分
                                    replaceValue = value.replaceFirst(value.substring(frontIdx, lastIdx), replaceValue);
                                }
                                BeanUtils.setProperty(obj, fild, replaceValue);
                            }
                        } catch (Throwable ex) {
                            log.error("obscure field error.", ex);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    @Override
    public void setProperties(Properties properties) {
        
    }
    
}
