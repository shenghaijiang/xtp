
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.RoleMenuOperation;
import cn.xtits.xtp.entity.RoleMenuOperationExample;
import cn.xtits.xtp.mapper.RoleMenuOperationMapper;
import cn.xtits.xtp.service.RoleMenuOperationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class RoleMenuOperationServiceImpl implements RoleMenuOperationService {

    @Resource
    private RoleMenuOperationMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(RoleMenuOperation record) {
        return mapper.insert(record);
    }

    @Override
    public List<RoleMenuOperation> listByExample(RoleMenuOperationExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public RoleMenuOperation getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(RoleMenuOperation record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateRoleMenuOperation(Integer roleId, List<RoleMenuOperation> roleMenuOperations) {
        RoleMenuOperationExample example = new RoleMenuOperationExample();
        RoleMenuOperationExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<RoleMenuOperation> list = mapper.selectByExample(example);
        for (RoleMenuOperation userMenuOperation : list) {
            mapper.deleteByPrimaryKey(userMenuOperation.getId());
        }
        for (RoleMenuOperation userMenu : roleMenuOperations) {
            userMenu.setRoleId(roleId);
            mapper.insert(userMenu);
        }
        return 1;
    }
}