/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac.cache;

import cn.com.bricks.mybatis.rbac.FilterConfig;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kete2003@gmail.com
 */
public class Table implements Serializable {

    private String name;
    private String sql;
    private Map<String, Field> mapFilter = new HashMap<String, Field>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getLowerName() {
        return name.toLowerCase();
    }

    /**
     * @param TableName
     */
    public void setName(String TableName) {
        this.name = TableName;
    }

    public void addFilter(String filterName, String fieldName, Integer operation) {

        if (!mapFilter.containsKey(filterName)) {
            Field field = new Field(fieldName, operation);
            mapFilter.put(filterName, field);
        }

    }

    public void generateSql(List<FilterConfig> lstConfigs) {

        if (lstConfigs.isEmpty()) {
            this.sql = "";
            return;
        }

        StringBuilder sb = new StringBuilder("");

        generateSql(lstConfigs, sb, 0L);

        //替换掉临时产生的"and ( )"
        int pos = sb.indexOf(" and  (  )");
        while (pos > 0) {
            sb.replace(pos, pos + 10, "");
            pos = sb.indexOf(" and  (  )");
        }

        if (0 == sb.indexOf(" (  ) ")) {
            sb.replace(0, 10, "");
        }

        //加入条件的完整sql语句
        StringBuilder total = new StringBuilder("(select * from ");
        total.append(this.name);

        if (sb.length() > 0) {
            total.append(" where ");
            total.append(sb);
        }

        total.append(")");

        sql = total.toString();

    }

    public void generateSql(List<FilterConfig> lstConfigs, StringBuilder sql, Long parent) {

        List<FilterConfig> configs = getSubList(lstConfigs, parent);
        if (configs.size() > 0) {
            if (!parent.equals(0L)) {
                sql.append(" and ");
            }
        } else {
            return;
        }

        sql.append(" ( ");

        int i = 0;
        for (FilterConfig fc : configs) {

            Field field = this.mapFilter.get(fc.getFilterName());
            if (null == field) {
                continue;
            }

            i++;

            //第二个条件开始用OR连接
            if (i > 1) {
                sql.append(" OR (");
            }

            sql.append(field.getFieldName());
            sql.append(" ");
            sql.append(fc.getOperationSymbol());

            //判断是否是between 或 in， 语句加一个括号
            boolean isBetweenOrIn = false;
            if (0 == fc.getOperationSymbol().compareTo("in")
                    || 0 == fc.getOperationSymbol().compareTo("between")) {
                isBetweenOrIn = true;
            }

            if (isBetweenOrIn) {
                sql.append("(");
            }

            //设定的值可以是多个，用“，”分割
            String[] values = fc.getFilterValue().split(",");
            int vi = 0;
            for (String vaule : values) {

                if (field.getOperation().equals(0)) {
                    sql.append("\'");
                    sql.append(getFiledValue(vaule));
                    sql.append("\'");
                } else {
                    // 如果操作类型非0，就将变量设定到sql中，
                    // 通过属性进行替换成sql参数中的内容
                    sql.append(getFiledValue(vaule));
                }

                ++vi;
                if (vi != values.length) {
                    sql.append(",");
                }
            }

            if (isBetweenOrIn) {
                sql.append(")");
            }

            //递归完成组合条件
            generateSql(lstConfigs, sql, fc.getId());

            //第二个条件开始用OR连接，括号收尾
            if (i > 1) {
                sql.append(") ");
            }
        }

        sql.append(" ) ");
    }

    //使得$成对出现，以便替换动态变量
    private String getFiledValue(String value) {
        if (value.length() > 0 && '$' == value.charAt(0)) {
            return value.trim() + "$";
        }

        return value.trim();
    }

    private List<FilterConfig> getSubList(List<FilterConfig> lstConfigs, Long parent) {
        List<FilterConfig> subList = new ArrayList<FilterConfig>();
        Iterator<FilterConfig> iter = lstConfigs.iterator();
        while (iter.hasNext()) {
            FilterConfig fc = iter.next();
            if (parent.equals(fc.getParentId())) {
                iter.remove();
                subList.add(fc);
            }
        }
        return subList;
    }

    /**
     * @param schema
     * @param mapParam
     * @return the sql
     * @throws java.sql.SQLException
     */
    public String getSql(StringBuffer schema, Map mapParam) throws SQLException {

        StringBuilder mapedSql = null;

        if (schema != null && schema.length() > 0) {
            schema.append(".");
            schema.append(name);
            mapedSql = new StringBuilder(sql.replace(name, schema.toString()));
        } else {
            mapedSql = new StringBuilder(sql);
        }

        if (mapParam != null && mapParam.size() > 0) {
            while (replaceDynamicParam(mapedSql, mapParam) >= 0) {
            }
        }

        if (mapedSql.indexOf("$") > 0) {
            throw new SQLException("ibatis 数据过滤还有动态变量没有赋值:" + mapedSql.toString());
        }

        return mapedSql.toString();
    }

    private int replaceDynamicParam(StringBuilder mapedSql, Map mapParam) throws SQLException {
        int first = mapedSql.indexOf("$");
        if (first <= 0) {
            return first;
        }

        int second = mapedSql.indexOf("$", first + 1);
        if (second <= 0) {
            return second;
        }

        String param = mapedSql.substring(first + 1, second);
        if (mapParam.containsKey(param)) {
            String value = mapParam.get(param).toString();
            mapedSql.replace(first, second + 1, value);
        } else {
            String msg = String.format("mybatis 数据过滤: 表%s需要传入参数%s", name, param);
            throw new SQLException(msg);
        }

        return second;

    }

    public void setSql(String string) {
        sql = string;
    }
}
