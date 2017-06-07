
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.Role;
import cn.xtits.xtp.entity.RoleExample;
import cn.xtits.xtp.mapper.RoleMapper;
import cn.xtits.xtp.service.RoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(Role record) {
        return mapper.insert(record);
    }

    @Override
    public List<Role> listByExample(RoleExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public Role getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(Role record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(Role record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

}