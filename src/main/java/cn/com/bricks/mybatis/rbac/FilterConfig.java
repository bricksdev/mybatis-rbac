package cn.com.bricks.mybatis.rbac;

import java.io.Serializable;


/**
 *
 * @author kete2003@gmail.com
 */
public class FilterConfig implements Cloneable, Serializable{


    private Long id;

    private String operaterId;

    private String filterName;

    private String filterValue;

    private String operationSymbol;

    private Long parentId;

    public FilterConfig() {
    }

    public FilterConfig(Long id) {
        this.id = id;
    }

    public FilterConfig(Long id, String roleId, String filterName,String operation,String filterValue,Long parent) {
        this.id = id;
        this.operaterId = roleId;
        this.filterName = filterName;
        this.filterValue = filterValue;
        this.operationSymbol = operation.trim().toLowerCase();
        this.parentId = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleId() {
        return operaterId;
    }

    public void setRoleId(String roleId) {
        this.operaterId = roleId;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterValue() {
        return filterValue;
    }


    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getOperationSymbol() {
        return operationSymbol;
    }

    public void setOperationSymbol(String operationSymbol) {
        this.operationSymbol = operationSymbol.trim().toLowerCase();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public Object clone()
    {
        try
        {
            return super.clone(); //call protected method
        } catch (CloneNotSupportedException e)
        {
            return null;
        }
    }

}
