
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.entity.UserOpen;
import cn.xtits.xtp.entity.UserOpenExample;
import cn.xtits.xtp.mapper.UserMapper;
import cn.xtits.xtp.mapper.base.UserOpenMapper;
import cn.xtits.xtp.service.UserOpenService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2019/7/26.
 */
@Service
public class UserOpenServiceImpl implements UserOpenService {

    @Resource
    private UserOpenMapper userOpenMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public int insertUserOpen(UserOpen record) {

        User user = new User();
        user.setAccount(record.getOpenId());
        user.setAppId(record.getAppId());
        user.setDeleteFlag(false);
        userMapper.insertSelective(user);
        record.setUserId(user.getId());
        return userOpenMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return userOpenMapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(UserOpen record) {
        return userOpenMapper.insert(record);
    }

    @Override
    public List<UserOpen> listByExample(UserOpenExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) userOpenMapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public UserOpen getByPrimaryKey(Integer ID) {
        return userOpenMapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(UserOpen record) {
        return userOpenMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserOpen record) {
        int count = userOpenMapper.updateByPrimaryKeySelective(record);
        return count;
    }
}