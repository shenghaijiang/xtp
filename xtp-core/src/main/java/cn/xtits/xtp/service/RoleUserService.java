package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.RoleUser;
import cn.xtits.xtp.entity.RoleUserExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface RoleUserService {

    int deleteByPrimaryKey(Integer ID);

    int insert(RoleUser record);

    List<RoleUser> listByExample(RoleUserExample example);

    RoleUser getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(RoleUser record);

    int updateRoleUser(Integer userId, String roleIds);
}