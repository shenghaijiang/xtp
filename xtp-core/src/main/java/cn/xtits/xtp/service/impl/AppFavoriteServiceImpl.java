
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.AppFavorite;
import cn.xtits.xtp.entity.AppFavoriteExample;
import cn.xtits.xtp.mapper.AppFavoriteMapper;
import cn.xtits.xtp.service.AppFavoriteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class AppFavoriteServiceImpl implements AppFavoriteService {

    @Resource
    private AppFavoriteMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(AppFavorite record) {
        return mapper.insert(record);
    }

    @Override
    public List<AppFavorite> listByExample(AppFavoriteExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public AppFavorite getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(AppFavorite record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateAppFavorite(Integer appId, String menuIds) {
        AppFavoriteExample example = new AppFavoriteExample();
        AppFavoriteExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        List<AppFavorite> list = mapper.selectByExample(example);
        for (AppFavorite appFavorite : list) {
            mapper.deleteByPrimaryKey(appFavorite.getId());
        }
        String[] ids = menuIds.split(",");
        for (String id : ids) {
            AppFavorite roleMenu = new AppFavorite();
            roleMenu.setAppId(appId);
            roleMenu.setMenuId(Integer.parseInt(id));
            mapper.insert(roleMenu);
        }
        return 1;
    }
}