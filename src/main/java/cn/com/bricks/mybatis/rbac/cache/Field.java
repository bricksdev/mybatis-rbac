/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.bricks.mybatis.rbac.cache;

import java.io.Serializable;

/**
 *
 * @author kete2003@gmail.com
 */
public class Field implements Serializable{

    private String fieldName;
    private Integer operation;
    private String fieldValue;

    public Field(String name, Integer operation){
        this.fieldName = name;
        this.operation = operation;
    }
    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the operation
     */
    public Integer getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    /**
     * @return the fieldValue
     */
    public Object getFieldValue() {
        return fieldValue;
    }

    /**
     * @param fieldValue the fieldValue to set
     */
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    

}
