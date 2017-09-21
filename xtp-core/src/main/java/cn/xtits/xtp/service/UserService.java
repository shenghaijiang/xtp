package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.entity.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface UserService {

    int deleteByPrimaryKey(Integer ID);

    int insert(User record);

    List<User> listByExample(UserExample example);

    User getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(User record);

    int updateByPrimaryKeySelective(User record);

    User getUserByAppUserId(Integer appUserId, Integer appId);

    List<User> listUserByRoleId( @Param("roleId") Integer roleId);
}