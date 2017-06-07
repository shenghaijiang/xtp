
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.Menu;
import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.entity.UserMenu;
import cn.xtits.xtp.entity.UserMenuExample;
import cn.xtits.xtp.mapper.UserMenuMapper;
import cn.xtits.xtp.service.UserMenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class UserMenuServiceImpl implements UserMenuService {

    @Resource
    private UserMenuMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(UserMenu record) {
        return mapper.insert(record);
    }

    @Override
    public List<UserMenu> listByExample(UserMenuExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public UserMenu getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(UserMenu record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateUserMenu(Integer userId, List<UserMenu> userMenus) {
        UserMenuExample example = new UserMenuExample();
        UserMenuExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserMenu> list = mapper.selectByExample(example);
        for (UserMenu userMenu : list) {
            mapper.deleteByPrimaryKey(userMenu.getId());
        }
        for (UserMenu userMenu : userMenus) {
            userMenu.setUserId(userId);
            mapper.insert(userMenu);
        }
        return 1;
    }
}