package cn.xtits.xtp.entity;

import java.io.Serializable;

public class RoleMenuOperation implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column roleMenuOperation.Id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column roleMenuOperation.RoleId
     *
     * @mbg.generated
     */
    private Integer roleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column roleMenuOperation.MenuOperationId
     *
     * @mbg.generated
     */
    private Integer menuOperationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column roleMenuOperation.MenuId
     *
     * @mbg.generated
     */
    private Integer menuId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column roleMenuOperation.Code
     *
     * @mbg.generated
     */
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table roleMenuOperation
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column roleMenuOperation.Id
     *
     * @return the value of roleMenuOperation.Id
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column roleMenuOperation.Id
     *
     * @param id the value for roleMenuOperation.Id
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column roleMenuOperation.RoleId
     *
     * @return the value of roleMenuOperation.RoleId
     * @mbg.generated
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column roleMenuOperation.RoleId
     *
     * @param roleId the value for roleMenuOperation.RoleId
     * @mbg.generated
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column roleMenuOperation.MenuOperationId
     *
     * @return the value of roleMenuOperation.MenuOperationId
     * @mbg.generated
     */
    public Integer getMenuOperationId() {
        return menuOperationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column roleMenuOperation.MenuOperationId
     *
     * @param menuOperationId the value for roleMenuOperation.MenuOperationId
     * @mbg.generated
     */
    public void setMenuOperationId(Integer menuOperationId) {
        this.menuOperationId = menuOperationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column roleMenuOperation.MenuId
     *
     * @return the value of roleMenuOperation.MenuId
     * @mbg.generated
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column roleMenuOperation.MenuId
     *
     * @param menuId the value for roleMenuOperation.MenuId
     * @mbg.generated
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column roleMenuOperation.Code
     *
     * @return the value of roleMenuOperation.Code
     * @mbg.generated
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column roleMenuOperation.Code
     *
     * @param code the value for roleMenuOperation.Code
     * @mbg.generated
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}