/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import java.util.List;

/**
 * 客户端用户角色服务
 *
 * @author kete2003@gmail.com
 */
public abstract class ClientRoleService {

    /**
     * 当前用户角色
     */
    private static final ThreadLocal<List<String>> CURRENT_USER_ROLES = new InheritableThreadLocal<List<String>>();

    public static void setCurrentUserRoles(List<String> roles) {
        CURRENT_USER_ROLES.set(roles);
    }

    public static List<String> getCurrentUserRoles() {
        return CURRENT_USER_ROLES.get();
    }
}
