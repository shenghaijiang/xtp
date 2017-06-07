
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.RoleUser;
import cn.xtits.xtp.entity.RoleUserExample;
import cn.xtits.xtp.mapper.RoleUserMapper;
import cn.xtits.xtp.service.RoleUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

    @Resource
    private RoleUserMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(RoleUser record) {
        return mapper.insert(record);
    }

    @Override
    public List<RoleUser> listByExample(RoleUserExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public RoleUser getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(RoleUser record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateRoleUser(Integer userId, String roleIds) {
        RoleUserExample example = new RoleUserExample();
        RoleUserExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<RoleUser> list = mapper.selectByExample(example);
        for (RoleUser roleUser : list) {
            mapper.deleteByPrimaryKey(roleUser.getId());
        }
        String[] ids = roleIds.split(",");
        for (String id : ids) {
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(userId);
            roleUser.setRoleId(Integer.parseInt(id));
            mapper.insert(roleUser);
        }
        return 1;
    }
}