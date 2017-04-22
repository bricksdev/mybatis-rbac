/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac.cache;

import cn.com.bricks.mybatis.rbac.ObscureConfig;
import cn.com.bricks.mybatis.rbac.ObscureFields;
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
public class ObscureCache {

    private final List<ObscureConfig> oconfigs;
    private final List<ObscureFields> ofields;

    private final Map<String, Map<String, Obscure>> mapObscureByRoles = new ConcurrentHashMap<String, Map<String, Obscure>>();

    public ObscureCache(List<ObscureConfig> oconfigs, List<ObscureFields> ofields) {
        this.oconfigs = oconfigs;
        this.ofields = ofields;
    }

    public Map<String, Obscure> getObscureConfigs(List<String> roleIds) {
        String key = getKey(roleIds);

        Map<String, Obscure> t = mapObscureByRoles.get(key);
        // 建立缓存数据
        if (null == t) {
            t = findObscures(roleIds);
            mapObscureByRoles.put(key, t);
        }

        return t;
    }

    public void dispose() {
        for (Map<String, Obscure> t : mapObscureByRoles.values()) {
            t.clear();
            t = null;
        }
        mapObscureByRoles.clear();
    }

    /**
     * 根据角色找出其对应的哪些表，哪些字段需要做数据过滤
     *
     * @param roles
     * @return
     */
    private Map<String, Obscure> findObscures(List<String> roles) {

        Map<String, Obscure> mapTables = new HashMap<String, Obscure>();

        if (oconfigs != null) {
            for (ObscureConfig ocf : oconfigs) {

                for (String role : roles) {
                    if (role.equals(ocf.getRoleId())) {
                        // 设定别名对应的混淆配置
                        if (!mapTables.containsKey(ocf.getObscureAlias())) {
                            mapTables.put(ocf.getObscureAlias(), new Obscure(ocf.getObscureAlias(), ocf.getObscureValue(), ocf.getObscureType()));
                        }
                    }
                }
            }
        }

        if (ofields != null) {
            Map<String, Map<String, List<String>>> fieldMap = new HashMap<String, Map<String, List<String>>>();
            for (ObscureFields ofds : ofields) {
                if (!fieldMap.containsKey(ofds.getObscureAlias())) {
                    fieldMap.put(ofds.getObscureAlias(), new HashMap<String, List<String>>());
                }

                // 保存类属性
                if (!fieldMap.get(ofds.getObscureAlias()).containsKey(ofds.getClassName())) {
                    fieldMap.get(ofds.getObscureAlias()).put(ofds.getClassName(), new ArrayList<String>());
                }
                // 暂时只处理0类型的处理，即字符类型的处理
                if (ofds.getFieldType() == 0) {
                    // 添加类属性
                    fieldMap.get(ofds.getObscureAlias()).get(ofds.getClassName()).add(ofds.getFieldName());
                }
            }

            // 组装到主体属性上
            for (Map.Entry<String, Map<String, List<String>>> entry : fieldMap.entrySet()) {
                if (mapTables.containsKey(entry.getKey())) {
                    // 设定关联表数据
                    List<ObscureField> obfields = new ArrayList<ObscureField>();
                    for (Map.Entry<String, List<String>> etry : entry.getValue().entrySet()) {
                        obfields.add(new ObscureField(etry.getKey(), etry.getValue()));
                    }
                    mapTables.get(entry.getKey()).setFields(obfields);
                }
            }
        }

        return mapTables;
    }

    private String getKey(List<String> roles) {
        Collections.sort(roles);

        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append("-");
        }
        return sb.toString();
    }

}
