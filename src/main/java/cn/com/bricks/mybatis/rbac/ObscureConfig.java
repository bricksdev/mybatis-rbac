package cn.com.bricks.mybatis.rbac;

import java.io.Serializable;

/**
 * 混淆配置类
 *
 * @author kete2003@gmail.com
 */
public class ObscureConfig implements Cloneable, Serializable {

    /**
     * 权限ID
     *
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 权限ID
     *
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * 混淆别名
     *
     * @return the obscureAlias
     */
    public String getObscureAlias() {
        return obscureAlias;
    }

    /**
     * 混淆别名
     *
     * @param obscureAlias the obscureAlias to set
     */
    public void setObscureAlias(String obscureAlias) {
        this.obscureAlias = obscureAlias;
    }

    /**
     * 混淆内容
     *
     * @return the obscureValue
     */
    public String getObscureValue() {
        return obscureValue;
    }

    /**
     * 混淆内容
     *
     * @param obscureValue the obscureValue to set
     */
    public void setObscureValue(String obscureValue) {
        this.obscureValue = obscureValue;
    }

    /**
     * 过滤类型
     *
     * @return the obscureType
     */
    public String getObscureType() {
        return obscureType;
    }

    /**
     * 过滤类型
     *
     * @param obscureType the obscureType to set
     */
    public void setObscureType(String obscureType) {
        this.obscureType = obscureType;
    }

    private Long id;

    /**
     * 权限ID
     */
    private String roleId;
    /**
     * 混淆别名
     */
    private String obscureAlias;
    /**
     * 混淆内容
     */
    private String obscureValue;

    /**
     * 过滤类型
     */
    private String obscureType;

    public ObscureConfig() {
    }

    public ObscureConfig(Long id) {
        this.id = id;
    }

    public ObscureConfig(Long id, String roleId, String obscureAlias, String obscureType, String obscureValue) {
        this.id = id;
        this.roleId = roleId;
        this.obscureAlias = obscureAlias;
        this.obscureValue = obscureValue;
        this.obscureType = obscureType;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
