/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import cn.com.bricks.mybatis.rbac.cache.Table;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author kete2003@gmail.com
 */
public class FilterSql implements Serializable {

    private StringBuffer newSql;
    private String lowerSql;

    public FilterSql(String sql) {
        newSql = new StringBuffer(sql);
        lowerSql = sql.toLowerCase();

    }

    /**
     * 针对sql中一个表进行数据过滤，忽略表名的大小写
     *
     * @param sql
     * @param table
     * @return
     */
    public void replace(Table table, Map mapParam) throws SQLException {

        String lowerTableName = table.getLowerName();
        int lenTableName = lowerTableName.length();

        int lenIncrease = 0;
        int pos = lowerSql.indexOf(lowerTableName);
        while (pos > 0) {

            StringBuffer schema = new StringBuffer();

            //判断表名是否完全匹配？并且是否带有schema
            if (compareTableName(lowerSql, pos, lenTableName, schema)) {
                int start = pos + lenIncrease;
                int lenSchema = schema.length();

                String tableSql = table.getSql(schema, mapParam);
                if (lenSchema > 0) {
                    start = start - lenSchema - 1;
                    newSql.replace(start, start + lenTableName + lenSchema + 1, tableSql);
                } else {
                    newSql.replace(start, start + lenTableName, tableSql);
                }

                //判断表后面是否带有别名
                if (isTableHasAlias(lowerSql, pos + lenTableName)) {
                    //带别名的话
                    lenIncrease += tableSql.length() - lenTableName;
                } else {
                    //不带别名的话
                    newSql.insert(start + tableSql.length(), " ");
                    newSql.insert(start + tableSql.length() + 1, table.getName());
                    lenIncrease += tableSql.length() + 1;
                }
            }

            pos = lowerSql.indexOf(lowerTableName, pos + 1);
        }

        if (lenIncrease > 0) {
            lowerSql = newSql.toString().toLowerCase();
        }

    }

    public boolean isTableHasAlias(String sql, int pos) {

        //表名后面的字符串
        String last = sql.substring(pos).trim();

        //为空,则肯定没有带别名
        if (0 == last.length()) {
            return false;
        }

        //如果为,或者)，也可确定没有带别名
        if (',' == last.charAt(0) || ')' == last.charAt(0)) {
            return false;
        }

        if (0 == last.indexOf("union")) {
            return false;
        }

        return isTableHasAliasEx(last, pos);
    }

    private boolean isTableHasAliasEx(String last, int pos) {
        int posWhere = last.indexOf("where ");
        int posOn = last.indexOf(" on ");

        //找不到条件语句，暂时认为是带别名的
        if (posWhere < 0 && posOn < 0) {
            return true;
        }

        //最小的,大于等于0的位置
        posWhere = ((posOn < posWhere) && (posOn >= 0)) || (posWhere < 0) ? posOn : posWhere;

        String beforeWhere = last.substring(0, posWhere).trim();
        if (!containsSplitToken(beforeWhere)) {
            return true;
        }

        int posSelect = last.indexOf("select ");
        //where属于后面select的条件语句
        if (posSelect > 0 && posSelect < posWhere) {
            return true;
        }

        // 获取别名
        int posComma = last.indexOf(",");
        if (pos < 0) {
            posComma = posWhere;
        }
        // 将and做分解，如果and中包含有对应的别名，就认为存在别名
        if (posComma > 0) {
            String aliasTableName = last.substring(0, posComma).trim();
            if (posWhere < last.length() && aliasTableName.length() > 0) {
                String[] andSplits = last.substring(posWhere, last.length()).split(" and ");
                for (String adsplit : andSplits) {
                    if (adsplit.contains(aliasTableName + ".")) {
                        return true;
                    }
                }
            }
        }
        int posCompare = last.indexOf("=");
        if (posCompare < 0) {
            posCompare = last.indexOf(">");
            if (posCompare < 0) {
                posCompare = last.indexOf("<");
                if (posCompare < 0) {
                    posCompare = last.indexOf(" like ");
                    if (posCompare < 0) {
                        posCompare = last.indexOf(" between");
                        if (posCompare < 0) {
                            posCompare = last.indexOf(" in");
                            if (posCompare < 0) {
                                posCompare = last.indexOf(" is ");
                            }
                        }
                    }
                }
            }
        }

        //如果在where 和 比较符合之间没有发现符合".",则认为没有带别名
        if (posCompare > posWhere) {
            String sub = last.substring(posWhere, posCompare);
            if (sub.indexOf(".") < 0) {
                return false;
            }
        }

        return true;
    }

    private boolean compareTableName(String sql, int pos, int lenTableName, StringBuffer schema) {

        if (pos <= 0) {
            return false;
        }

        char ch;

        //比较前一字符
        ch = sql.charAt(pos - 1);
        if (!isSplitToken(ch)) {

            //取得schema
            if ('.' == ch) {
                int i = pos - 1;
                while (i > 0) {
                    i--;
                    if (isSplitToken(sql.charAt(i))) {
                        break;
                    }
                }

                schema.append(sql.substring(i + 1, pos - 1));
            } else {
                return false;
            }
        }

        int lastPos = pos + lenTableName;
        if (lastPos >= sql.length()) {
            return true;
        }

        //比较后一个字符
        ch = sql.charAt(pos + lenTableName);
        return isSplitToken(ch);
    }

    /**
     * 是否为分割符
     *
     * @param ch
     * @return
     */
    private boolean isSplitToken(char ch) {
        if (' ' == ch || ',' == ch || '/' == ch || '(' == ch || ')' == ch) {
            return true;
        } else {
            return false;
        }
    }

    private boolean containsSplitToken(String str) {

        for (int i = 0; i < str.length(); i++) {
            if (isSplitToken(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the lowerSql
     */
    public String getLowerSql() {
        return lowerSql;
    }

    /**
     * @return the newSql
     */
    public String getNewSql() {
        return newSql.toString();
    }

    public boolean containTable(Table table) {
        if (lowerSql.indexOf(table.getLowerName()) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
