package cn.xtits.xtp.mapper;

import cn.xtits.xtp.entity.UserMenuOperation;
import cn.xtits.xtp.entity.UserMenuOperationExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserMenuOperationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    long countByExample(UserMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int insert(UserMenuOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int insertSelective(UserMenuOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    List<UserMenuOperation> selectByExample(UserMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    UserMenuOperation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") UserMenuOperation record, @Param("example") UserMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") UserMenuOperation record, @Param("example") UserMenuOperationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserMenuOperation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usermenuoperation
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserMenuOperation record);
}