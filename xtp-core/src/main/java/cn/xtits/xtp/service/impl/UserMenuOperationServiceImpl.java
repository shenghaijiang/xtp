
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.UserMenuOperation;
import cn.xtits.xtp.entity.UserMenuOperationExample;
import cn.xtits.xtp.mapper.UserMenuOperationMapper;
import cn.xtits.xtp.service.UserMenuOperationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class UserMenuOperationServiceImpl implements UserMenuOperationService {

    @Resource
    private UserMenuOperationMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(UserMenuOperation record) {
        return mapper.insert(record);
    }

    @Override
    public List<UserMenuOperation> listByExample(UserMenuOperationExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public UserMenuOperation getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(UserMenuOperation record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateUserMenuOperation(Integer userId, Integer menuId, List<UserMenuOperation> userMenuOperations) {
        UserMenuOperationExample example = new UserMenuOperationExample();
        UserMenuOperationExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andMenuIdEqualTo(menuId);
        List<UserMenuOperation> list = mapper.selectByExample(example);
        for (UserMenuOperation userMenuOperation : list) {
            mapper.deleteByPrimaryKey(userMenuOperation.getId());
        }
        for (UserMenuOperation userMenu : userMenuOperations) {
            userMenu.setUserId(userId);
            userMenu.setMenuId(menuId);
            mapper.insert(userMenu);
        }
        return 1;
    }
}