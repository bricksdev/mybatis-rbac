/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac.cache;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author kete2003@gmail.com
 */
public class Obscure implements Serializable {

    private String obscureAlias;
    /**
     * 类属性
     */
    private List<ObscureField> fields;
    /**
     * 混淆内容
     */
    private String obscureValue;
    /**
     * 混淆类型
     */
    private String obscureType;

    public Obscure(String obscureAlias) {
        this.obscureAlias = obscureAlias;
    }

    public Obscure(String obscureAlias, String obscureValue, String obscureType) {
        this.obscureAlias = obscureAlias;
        this.obscureValue = obscureValue;
        this.obscureType = obscureType;
    }

    public Obscure() {
    }

    public void setFields(List<ObscureField> fields) {
        this.fields = fields;
    }

    public List<ObscureField> getFields() {
        return fields;
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
     * 混淆类型
     *
     * @return the obscureType
     */
    public String getObscureType() {
        return obscureType;
    }

    /**
     * 混淆类型
     *
     * @param obscureType the obscureType to set
     */
    public void setObscureType(String obscureType) {
        this.obscureType = obscureType;
    }

    /**
     * @return the obscureAlias
     */
    public String getObscureAlias() {
        return obscureAlias;
    }

    /**
     * @param obscureAlias the obscureAlias to set
     */
    public void setObscureAlias(String obscureAlias) {
        this.obscureAlias = obscureAlias;
    }
}
