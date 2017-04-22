package cn.com.bricks.mybatis.rbac;

import java.io.Serializable;

/**
 *
 * @author kete2003@gmail.com
 */
public class FilterFields implements Serializable{

    private Long id;

    private String filterName;

    private String tableName;

    private String fieldName;

    private Integer fieldType;

    public FilterFields() {
    }

    public FilterFields(Long id) {
        this.id = id;
    }

    public FilterFields(Long id, String filterName, String tableName, String fieldName,Integer fieldType) {
        this.id = id;
        this.filterName = filterName;
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getFieldType() {
        return fieldType;
    }

    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }


}
