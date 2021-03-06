package cn.xtits.xtp.mapper;

import cn.xtits.xtp.entity.RoleMenuOperation;
import cn.xtits.xtp.entity.RoleMenuOperationExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleMenuOperationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    long countByExample(RoleMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int insert(RoleMenuOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int insertSelective(RoleMenuOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    List<RoleMenuOperation> selectByExample(RoleMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    RoleMenuOperation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") RoleMenuOperation record, @Param("example") RoleMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") RoleMenuOperation record, @Param("example") RoleMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(RoleMenuOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table rolemenuoperation
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(RoleMenuOperation record);
}