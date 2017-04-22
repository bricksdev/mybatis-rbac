package cn.com.bricks.mybatis.rbac;

/**
 *
 * @author kete2003@gmail.com
 */
public class ObscureFields {

    private long id;
    /**
     * 过滤别名
     */
    private String obscureAlias;
    /**
     * 类全名
     */
    private String className;
    /**
     * 类属性
     */
    private String fieldName;
    /**
     * 属性类型
     */
    private int fieldType;

    public ObscureFields() {
    }

    public ObscureFields(long id) {
        this.id = id;
    }

    public ObscureFields(long id, String filterAlias, String className, String fieldName, int fieldType) {
        this.id = id;
        this.obscureAlias = filterAlias;
        this.className = className;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
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

    /**
     * 属性类型
     *
     * @param fieldType the fieldType to set
     */
    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
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
     * 过滤别名
     *
     * @return the obscureAlias
     */
    public String getObscureAlias() {
        return obscureAlias;
    }

    /**
     * 过滤别名
     *
     * @param obscureAlias the obscureAlias to set
     */
    public void setObscureAlias(String obscureAlias) {
        this.obscureAlias = obscureAlias;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

}
