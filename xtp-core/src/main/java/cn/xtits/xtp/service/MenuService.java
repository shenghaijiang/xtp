package cn.xtits.xtp.service;

import cn.xtits.xtp.dto.UserMenuDto;
import cn.xtits.xtp.dto.UserMenuWithOperationDto;
import cn.xtits.xtp.entity.Menu;
import cn.xtits.xtp.entity.MenuExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface MenuService {

    int deleteByPrimaryKey(Integer ID);

    int insert(Menu record);

    List<Menu> listByExample(MenuExample example);

    Menu getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(Menu record);

    UserMenuDto listMenuByUserId(Integer userId);

    UserMenuWithOperationDto listMenuWithOperationByUserId(Integer userId);

}