
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.dto.MenuWithOperationDto;
import cn.xtits.xtp.dto.UserMenuDto;
import cn.xtits.xtp.dto.UserMenuWithOperationDto;
import cn.xtits.xtp.entity.*;
import cn.xtits.xtp.mapper.AppFavoriteMapper;
import cn.xtits.xtp.mapper.MenuMapper;
import cn.xtits.xtp.mapper.UserFavoriteMapper;
import cn.xtits.xtp.mapper.UserMapper;
import cn.xtits.xtp.service.AppFavoriteService;
import cn.xtits.xtp.service.MenuService;
import cn.xtits.xtp.service.UserFavoriteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private AppFavoriteMapper appFavoriteMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private UserFavoriteMapper uerFavoriteMapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return menuMapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(Menu record) {
        return menuMapper.insert(record);
    }

    @Override
    public List<Menu> listByExample(MenuExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) menuMapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public Menu getByPrimaryKey(Integer ID) {
        return menuMapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(Menu record) {
        return menuMapper.updateByPrimaryKey(record);
    }

    @Override
    public UserMenuDto listMenuByUserId(Integer userId) {

        //TODO 加缓存
        User user = userMapper.selectByPrimaryKey(userId);
        Integer appId = user.getAppId();
        UserMenuDto dto = new UserMenuDto();
        //所有
        List<Menu> allList = menuMapper.listMenuByUserId(null, userId);
        dto.setAllMenu(allList);

        //用户常用
        List<Menu> userFavoriteReturnList = new ArrayList<>();
        UserFavoriteExample example = new UserFavoriteExample();
        UserFavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserFavorite> list = uerFavoriteMapper.selectByExample(example);
        for (Menu menu : allList) {
            for (UserFavorite favorite : list) {
                if (menu.getId().equals(favorite.getMenuId())) {
                    userFavoriteReturnList.add(menu);
                    break;
                }
            }
        }
        dto.setUserFavoriteMenu(userFavoriteReturnList);

        //应用常用
        List<Menu> appFavoriteReturnList = new ArrayList<>();
        AppFavoriteExample example2 = new AppFavoriteExample();
        AppFavoriteExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andAppIdEqualTo(appId);
        List<AppFavorite> list2 = appFavoriteMapper.selectByExample(example2);
        for (Menu menu : allList) {
            for (AppFavorite favorite : list2) {
                if (menu.getId().equals(favorite.getMenuId())) {
                    appFavoriteReturnList.add(menu);
                    break;
                }
            }
        }
        dto.setAppFavoriteMenu(appFavoriteReturnList);

        //第一级
        List<Menu> parentMenuReturnList = new ArrayList<>();
        for (Menu menu : allList) {
            if (menu.getParentFlag() && menu.getParentId() == 0) {
                parentMenuReturnList.add(menu);
            }
        }

        dto.setParentMenu(parentMenuReturnList);
        return dto;
    }

    @Override
    public UserMenuWithOperationDto listMenuWithOperationByUserId(Integer userId) {

        //TODO 加缓存
        User user = userMapper.selectByPrimaryKey(userId);
        Integer appId = user.getAppId();
        UserMenuWithOperationDto dto = new UserMenuWithOperationDto();
        //所有
        List<MenuWithOperationDto> repeatlist = menuMapper.listMenuWithOperationByUserId(null, userId);
        List<MenuWithOperationDto> allList = new ArrayList<>();

        MenuWithOperationDto temp = new MenuWithOperationDto();
        temp.setId(0);


        for (MenuWithOperationDto item : repeatlist) {
            MenuOperation e = new MenuOperation();
            if(item.getOperationId()!=null) {
                e.setId(item.getOperationId());
                e.setCode(item.getOperationCoce());
                e.setName(item.getOperationName());
            }
            item.setOperationId(null);
            item.setOperationCoce(null);
            item.setOperationName(null);

            //TODO 优化 第一个元素处理
            if (temp.getId().equals(0)) {
                temp = item;
                temp.setOperationList(new ArrayList<>());
                if(e.getId()!=null) {
                    temp.getOperationList().add(e);
                }
                allList.add(temp);
                continue;
            }

            if (item.getId().equals(temp.getId())) {

                if(e.getId()!=null) {
                    temp.getOperationList().add(e);
                }

            } else {//相同的时候添加选项
                temp = item;
                if(e.getId()!=null) {
                    temp.getOperationList().add(e);
                }
                temp.setOperationList(new ArrayList<>());

                allList.add(temp);
            }
        }

        dto.setAllMenu(allList);

        //用户常用
        List<MenuWithOperationDto> userFavoriteReturnList = new ArrayList<>();
        UserFavoriteExample example = new UserFavoriteExample();
        UserFavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserFavorite> list = uerFavoriteMapper.selectByExample(example);
        for (MenuWithOperationDto menu : allList) {
            for (UserFavorite favorite : list) {
                if (menu.getId().equals(favorite.getMenuId())) {
                    userFavoriteReturnList.add(menu);
                    break;
                }
            }
        }
        dto.setUserFavoriteMenu(userFavoriteReturnList);

        //应用常用
        List<MenuWithOperationDto> appFavoriteReturnList = new ArrayList<>();
        AppFavoriteExample example2 = new AppFavoriteExample();
        AppFavoriteExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andAppIdEqualTo(appId);
        List<AppFavorite> list2 = appFavoriteMapper.selectByExample(example2);
        for (MenuWithOperationDto menu : allList) {
            for (AppFavorite favorite : list2) {
                if (menu.getId().equals(favorite.getMenuId())) {
                    appFavoriteReturnList.add(menu);
                    break;
                }
            }
        }
        dto.setAppFavoriteMenu(appFavoriteReturnList);

        //第一级
        List<MenuWithOperationDto> parentMenuReturnList = new ArrayList<>();
        for (MenuWithOperationDto menu : allList) {
            if (menu.getParentFlag() && menu.getParentId() == 0) {
                parentMenuReturnList.add(menu);
            }
        }
        dto.setParentMenu(parentMenuReturnList);
        return dto;
    }

}