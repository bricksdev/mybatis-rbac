/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

/**
 *
 * @author kete2003@gmail.com
 */
public class DomainBean {

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the materialCode
     */
    public String getMaterialCode() {
        return materialCode;
    }

    /**
     * @param materialCode the materialCode to set
     */
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    
    private long id;
    private String price;
    private String materialCode;
    private String phone;
    private String name;

    public DomainBean() {
    }

    public DomainBean(long id, String price, String materialCode) {
        this.id = id;
        this.price = price;
        this.materialCode = materialCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(price).append("-").append(materialCode).append("-").append(id).append("-").append(phone).append("-").append(name);
        return builder.toString();
    }
    
    
}
