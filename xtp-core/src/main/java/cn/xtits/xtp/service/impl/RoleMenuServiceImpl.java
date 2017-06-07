
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.RoleExample;
import cn.xtits.xtp.entity.RoleMenu;
import cn.xtits.xtp.entity.RoleMenuExample;
import cn.xtits.xtp.entity.UserExample;
import cn.xtits.xtp.mapper.RoleMenuMapper;
import cn.xtits.xtp.service.RoleMenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(RoleMenu record) {
        return mapper.insert(record);
    }

    @Override
    public List<RoleMenu> listByExample(RoleMenuExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public RoleMenu getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(RoleMenu record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateRoleMenu(Integer roleId, String menuIds) {
        RoleMenuExample example = new RoleMenuExample();
        RoleMenuExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<RoleMenu> list = mapper.selectByExample(example);
        for (RoleMenu roleMenu : list) {
            mapper.deleteByPrimaryKey(roleMenu.getId());
        }
        String[] ids = menuIds.split(",");
        for (String id : ids) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(Integer.parseInt(id));
            mapper.insert(roleMenu);
        }
        return 1;
    }
}