package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.Menu;
import cn.xtits.xtp.entity.UserMenu;
import cn.xtits.xtp.entity.UserMenuExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface UserMenuService {

    int deleteByPrimaryKey(Integer ID);

    int insert(UserMenu record);

    List<UserMenu> listByExample(UserMenuExample example);

    UserMenu getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(UserMenu record);

    int updateUserMenu(Integer userId, List<UserMenu> userMenus);
}