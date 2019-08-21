
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
    public int updateCopy(Integer fromAppId, Integer toAppId) {

        MenuExample example = new MenuExample();
        example.setPageIndex(1);
        example.setPageSize(Integer.MAX_VALUE);
        MenuExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(fromAppId);
        criteria.andDeleteFlagEqualTo(false);

        List<Menu> list = menuMapper.selectByExample(example);

        List<Menu> level0List = new ArrayList<>();
        List<Menu> level1List = new ArrayList<>();
        List<Menu> level2List = new ArrayList<>();
        for (Menu menu : list) {
            if (menu.getParentId().equals(0))
                level0List.add(menu);
        }

        Integer currentParentId1 = 0;
        Integer currentParentId2 = 0;
        for (Menu menu0 : level0List) {
            Menu insertMenu = new Menu();
            insertMenu.setAppId(toAppId);
            insertMenu.setCode(menu0.getCode());
            insertMenu.setName(menu0.getName());
            insertMenu.setDeleteFlag(false);
            insertMenu.setDisplayFlag(menu0.getDisplayFlag());
            insertMenu.setEnableFlag(menu0.getEnableFlag());
            insertMenu.setParentFlag(menu0.getParentFlag());
            insertMenu.setParentId(menu0.getParentId());
            insertMenu.setType(menu0.getType());
            insertMenu.setUrl(menu0.getUrl());
            insertMenu.setSort(menu0.getSort());
            insertMenu.setIcon(menu0.getIcon());
            //0级插入
            menuMapper.insertSelective(insertMenu);
            currentParentId1 = insertMenu.getId();
            //1级插入
            for (Menu menu1 : list) {
                if(menu1.getParentId().equals(menu0.getId())){
                    insertMenu = new Menu();
                    insertMenu.setAppId(toAppId);
                    insertMenu.setCode(menu1.getCode());
                    insertMenu.setName(menu1.getName());
                    insertMenu.setDeleteFlag(false);
                    insertMenu.setDisplayFlag(menu1.getDisplayFlag());
                    insertMenu.setEnableFlag(menu1.getEnableFlag());
                    insertMenu.setParentFlag(menu1.getParentFlag());
                    insertMenu.setParentId(currentParentId1);
                    insertMenu.setType(menu1.getType());
                    insertMenu.setUrl(menu1.getUrl());
                    insertMenu.setSort(menu1.getSort());
                    insertMenu.setIcon(menu1.getIcon());
                    menuMapper.insertSelective(insertMenu);
                    currentParentId2 = insertMenu.getId();

                    //2级插入
                    for (Menu menu2 : list) {
                        if(menu2.getParentId().equals(menu1.getId())){
                            insertMenu = new Menu();
                            insertMenu.setAppId(toAppId);
                            insertMenu.setCode(menu2.getCode());
                            insertMenu.setName(menu2.getName());
                            insertMenu.setDeleteFlag(false);
                            insertMenu.setDisplayFlag(menu2.getDisplayFlag());
                            insertMenu.setEnableFlag(menu2.getEnableFlag());
                            insertMenu.setParentFlag(menu2.getParentFlag());
                            insertMenu.setParentId(currentParentId2);
                            insertMenu.setType(menu2.getType());
                            insertMenu.setUrl(menu2.getUrl());
                            insertMenu.setSort(menu2.getSort());
                            insertMenu.setIcon(menu2.getIcon());
                            menuMapper.insertSelective(insertMenu);
                        }
                    }
                }
            }
        }

        return 1;
    }

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
            //是否有按钮权限
            MenuOperation menuOperation = new MenuOperation();
            if (item.getOperationId() != null) {
                menuOperation.setId(item.getOperationId());
                menuOperation.setCode(item.getOperationCoce());
                menuOperation.setName(item.getOperationName());
            }
            item.setOperationId(null);
            item.setOperationCoce(null);
            item.setOperationName(null);

            //TODO 优化 第一个元素处理
            if (temp.getId().equals(0)) {
                temp = item;
                temp.setOperationList(new ArrayList<>());
                if (menuOperation.getId() != null) {
                    temp.getOperationList().add(menuOperation);
                }
                allList.add(temp);
                continue;
            }
            //相同的时候添加选项
            if (item.getId().equals(temp.getId())) {

                if (menuOperation.getId() != null) {
                    if (temp.getOperationList() == null) {
                        temp.setOperationList(new ArrayList<>());
                    }
                    temp.getOperationList().add(menuOperation);
                }
            //不相同的时候添加菜单
            } else {
                temp = item;
                temp.setOperationList(new ArrayList<>());
                if (menuOperation.getId() != null) {
                    temp.getOperationList().add(menuOperation);
                }
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