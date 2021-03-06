package cn.xtits.xtp.entity;

import java.io.Serializable;

public class UserMenuOperation implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column userMenuOperation.Id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column userMenuOperation.UserId
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column userMenuOperation.MenuOperationId
     *
     * @mbg.generated
     */
    private Integer menuOperationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column userMenuOperation.Code
     *
     * @mbg.generated
     */
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column userMenuOperation.Type
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column userMenuOperation.MenuId
     *
     * @mbg.generated
     */
    private Integer menuId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table userMenuOperation
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column userMenuOperation.Id
     *
     * @return the value of userMenuOperation.Id
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column userMenuOperation.Id
     *
     * @param id the value for userMenuOperation.Id
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column userMenuOperation.UserId
     *
     * @return the value of userMenuOperation.UserId
     * @mbg.generated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column userMenuOperation.UserId
     *
     * @param userId the value for userMenuOperation.UserId
     * @mbg.generated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column userMenuOperation.MenuOperationId
     *
     * @return the value of userMenuOperation.MenuOperationId
     * @mbg.generated
     */
    public Integer getMenuOperationId() {
        return menuOperationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column userMenuOperation.MenuOperationId
     *
     * @param menuOperationId the value for userMenuOperation.MenuOperationId
     * @mbg.generated
     */
    public void setMenuOperationId(Integer menuOperationId) {
        this.menuOperationId = menuOperationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column userMenuOperation.Code
     *
     * @return the value of userMenuOperation.Code
     * @mbg.generated
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column userMenuOperation.Code
     *
     * @param code the value for userMenuOperation.Code
     * @mbg.generated
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column userMenuOperation.Type
     *
     * @return the value of userMenuOperation.Type
     * @mbg.generated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column userMenuOperation.Type
     *
     * @param type the value for userMenuOperation.Type
     * @mbg.generated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column userMenuOperation.MenuId
     *
     * @return the value of userMenuOperation.MenuId
     * @mbg.generated
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column userMenuOperation.MenuId
     *
     * @param menuId the value for userMenuOperation.MenuId
     * @mbg.generated
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}