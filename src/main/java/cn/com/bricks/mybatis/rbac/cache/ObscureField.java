/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac.cache;

import java.util.List;

/**
 *
 * @author kete2003@gmail.com
 */
public class ObscureField {

    private String className;
    private List<String> fields;

    public ObscureField(String className, List<String> fields) {
        this.className = className;
        this.fields = fields;
    }

    public ObscureField() {
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the fields
     */
    public List<String> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
