
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.UserFavorite;
import cn.xtits.xtp.entity.UserFavoriteExample;
import cn.xtits.xtp.mapper.UserFavoriteMapper;
import cn.xtits.xtp.service.UserFavoriteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {

    @Resource
    private UserFavoriteMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(UserFavorite record) {
        return mapper.insert(record);
    }

    @Override
    public List<UserFavorite> listByExample(UserFavoriteExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public UserFavorite getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(UserFavorite record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateUserFavorite(Integer userId, List<UserFavorite> userFavorites) {
        UserFavoriteExample example = new UserFavoriteExample();
        UserFavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserFavorite> list = mapper.selectByExample(example);
        for (UserFavorite userFavorite : list) {
            mapper.deleteByPrimaryKey(userFavorite.getId());
        }
        for (UserFavorite userFavorite : userFavorites) {
            userFavorite.setUserId(userId);
            mapper.insert(userFavorite);
        }
        return 1;
    }
}