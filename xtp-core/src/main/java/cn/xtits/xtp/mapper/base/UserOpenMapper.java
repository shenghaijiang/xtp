package cn.xtits.xtp.mapper.base;

import cn.xtits.xtp.entity.UserOpen;
import cn.xtits.xtp.entity.UserOpenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserOpenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    long countByExample(UserOpenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int insert(UserOpen record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int insertSelective(UserOpen record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    List<UserOpen> selectByExample(UserOpenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    UserOpen selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") UserOpen record, @Param("example") UserOpenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") UserOpen record, @Param("example") UserOpenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserOpen record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table useropen
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserOpen record);
}