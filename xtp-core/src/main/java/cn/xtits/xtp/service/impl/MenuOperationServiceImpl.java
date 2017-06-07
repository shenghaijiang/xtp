
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.MenuOperation;
import cn.xtits.xtp.entity.MenuOperationExample;
import cn.xtits.xtp.mapper.MenuOperationMapper;
import cn.xtits.xtp.service.MenuOperationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class MenuOperationServiceImpl implements MenuOperationService {

    @Resource
    private MenuOperationMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(MenuOperation record) {
        return mapper.insert(record);
    }

    @Override
    public List<MenuOperation> listByExample(MenuOperationExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public MenuOperation getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(MenuOperation record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(MenuOperation record) {
        return mapper.updateByPrimaryKeySelective(record);
    }
}