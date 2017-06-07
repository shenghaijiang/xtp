package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.Role;
import cn.xtits.xtp.entity.RoleExample;
import cn.xtits.xtp.entity.User;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface RoleService {

    int deleteByPrimaryKey(Integer ID);

    int insert(Role record);

    List<Role> listByExample(RoleExample example);

    Role getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(Role record);

    int updateByPrimaryKeySelective(Role record);
}