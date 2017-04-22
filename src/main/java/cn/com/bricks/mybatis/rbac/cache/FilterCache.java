/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac.cache;

import cn.com.bricks.mybatis.rbac.FilterConfig;
import cn.com.bricks.mybatis.rbac.FilterFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kete2003@gmail.com
 */
public class FilterCache {

    private final List<FilterConfig> lstFilterConfig;
    private final List<FilterFields> lstFilterField;
    private Map<String, Map<String, Table>> mapTablesByRoles = new ConcurrentHashMap<String, Map<String, Table>>();

    public FilterCache(List<FilterConfig> configs, List<FilterFields> fields) {
        this.lstFilterConfig = configs;
        this.lstFilterField = fields;
    }

    public Map<String, Table> getTables(List<String> roles) {

        String key = getKey(roles);

        Map<String, Table> t = mapTablesByRoles.get(key);
        if (null == t) {
            t = findTables(roles);
            mapTablesByRoles.put(key, t);
        }

        return t;
    }

    public void dispose() {
        for (Map<String, Table> t : mapTablesByRoles.values()) {
            t.clear();
            t = null;
        }
        mapTablesByRoles.clear();
        mapTablesByRoles = null;
    }

    private String getKey(List<String> roles) {
        Collections.sort(roles);

        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append("-");
        }
        return sb.toString();
    }

    /**
     * 根据角色找出其对应的哪些表，哪些字段需要做数据过滤
     * @param roles
     * @return
     */
    private Map<String, Table> findTables(List<String> roles) {

        Map<String, Table> mapTables = new HashMap<String, Table>();       
        //List<FilterConfig> validConfigs = new ArrayList<FilterConfig>();

        List<FilterConfig> configsParent0 = new ArrayList<FilterConfig>();
        List<FilterConfig> configsAll = new ArrayList<FilterConfig>();

        
        getFilterConfig(roles,configsParent0,configsAll);

        //找出config 父节点为0的filterName确定有多少个table
        findTableParent0(configsParent0, mapTables);

        //为table添加全部的过滤条件
        fullTablesFilter(configsAll, mapTables);
        
        //生成过滤Sql
        generateSql(mapTables, configsAll);

        return mapTables;
    }

    //找出config 父节点为0的filterName确定有多少个table
    private void findTableParent0(List<FilterConfig> configsParent0, Map<String, Table>mapTables){
         Map<String, String> filters = new HashMap<String, String>();  

        //找出角色对应的FilterName和FilterConfig
        findFilterConfigs(configsParent0, filters);

        //找出FilterName对应的表
        findTableByFilters(filters, mapTables);
    }

    //为table添加全部的过滤条件
     private void fullTablesFilter(List<FilterConfig> configsAll,  Map<String, Table>mapTables){
         Map<String, String> filters = new HashMap<String, String>();

        //找出角色对应的FilterName和FilterConfig
         findFilterConfigs(configsAll, filters);

        //找出FilterName对应的表
        findFilterFields(filters, mapTables);

    }

    private void getFilterConfig(List<String> roles,List<FilterConfig> configsParent0,List<FilterConfig> configsAll){

         for (FilterConfig config : lstFilterConfig) {

            for (String role : roles) {
                if (role.equals(config.getRoleId())) {

                    if(config.getParentId().equals(0L)){
                        configsParent0.add(config);
                    }

                    configsAll.add(config);
                }
            }
         }
         
    }

    /**
     * 找出角色对应的FilterName和FilterConfig
     * @param rolesConfig
     * @param filters
     */
    public void findFilterConfigs(List<FilterConfig> rolesConfig, Map<String, String> filters) {
        for (FilterConfig config : rolesConfig) {
            
            //记录角色对应的Filter
            String key = config.getFilterName();
            if (!filters.containsKey(key)) {
                filters.put(key, key);
            }
        }

    }

    /**
     * 找出FilterName对应的表
     * @param filters
     * @param tables
     */
    public void findTableByFilters(Map<String, String> filters, Map<String, Table> tables) {
        for (FilterFields field : lstFilterField) {
            if (filters.containsKey(field.getFilterName())) {

                String key = field.getTableName();
                Table table = tables.get(key);
                if (null == table) {
                    table = new Table();
                    table.setName(key);
                    tables.put(key, table);
                }

            }
        }
    }

        /**
     * 找出FilterName对应的表
     * @param filters
     * @param tables
     */
    public void findFilterFields(Map<String, String> filters, Map<String, Table> tables) {
        for (FilterFields field : lstFilterField) {
            if (filters.containsKey(field.getFilterName())) {

                String key = field.getTableName();
                Table table = tables.get(key);
                if (null != table) {
                    table.addFilter(field.getFilterName(), field.getFieldName(), field.getFieldType());
                }                
            }
        }
    }

    /**
     * 生成过滤Sql
     * @param mapTables
     * @param lstConfigs
     */
    private void generateSql(Map<String, Table> mapTables, List<FilterConfig> lstConfigs) {
        for (Table table : mapTables.values()) {

            //复制一份，防止在生成sql时,list数据丢失，导致其他Table生成Sql错误
            List<FilterConfig> copy = new ArrayList<FilterConfig>();
            for (FilterConfig fc : lstConfigs) {
                copy.add((FilterConfig) (fc.clone()));
            }

            table.generateSql(copy);
        }
    }
}
