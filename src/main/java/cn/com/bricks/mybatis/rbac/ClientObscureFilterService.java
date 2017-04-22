/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import cn.com.bricks.mybatis.rbac.cache.ObscureCache;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kete2003@gmail.com
 */
public class ClientObscureFilterService {

    private static ObscureCache obscureCache = new ObscureCache(new ArrayList<ObscureConfig>(), new ArrayList<ObscureFields>());

    public static void setObscureData(List<ObscureConfig> configs, List<ObscureFields> fields) {

        if (null != obscureCache) {
            obscureCache.dispose();
        }

        obscureCache = new ObscureCache(configs, fields);
    }

    public static ObscureCache getObscureData() {
        return obscureCache;
    }
}
