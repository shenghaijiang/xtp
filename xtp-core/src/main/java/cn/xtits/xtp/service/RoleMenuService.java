package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.RoleMenu;
import cn.xtits.xtp.entity.RoleMenuExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface RoleMenuService {

    int deleteByPrimaryKey(Integer ID);

    int insert(RoleMenu record);

    List<RoleMenu> listByExample(RoleMenuExample example);

    RoleMenu getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(RoleMenu record);

    int updateRoleMenu(Integer roleId, String menuIds);
}