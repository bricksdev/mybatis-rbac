/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import cn.com.bricks.mybatis.rbac.cache.FilterCache;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kete2003@gmail.com
 */
public abstract class ClientRoleFilterService {

    private static FilterCache filterCache = new FilterCache(new ArrayList<FilterConfig>(), new ArrayList<FilterFields>());

    public static void setFilterData(List<FilterConfig> configs, List<FilterFields> fields) {

        if (null != filterCache) {
            filterCache.dispose();
        }

        filterCache = new FilterCache(configs, fields);
    }

    public static FilterCache getFilterData() {
        return filterCache;
    }

}
