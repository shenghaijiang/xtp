
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.AppExample;
import cn.xtits.xtp.mapper.AppMapper;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.AppService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper mapper;

    @Override
    public App getAppByToken(String token) {
        return mapper.getAppByToken(token);
    }

    @Override
    public App getAppByCode(String code) {
        return mapper.getAppByCode(code);
    }

    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insert(App record) {
        mapper.insert(record);
        return 0;
    }

    @Override
    public List<App> listByExample(AppExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public App getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(App record) {
        return mapper.updateByPrimaryKey(record);
    }
}