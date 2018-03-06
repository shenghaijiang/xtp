
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.Organize;
import cn.xtits.xtp.entity.OrganizeExample;
import cn.xtits.xtp.mapper.OrganizeMapper;
import cn.xtits.xtp.service.OrganizeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class OrganizeServiceImpl implements OrganizeService {

    @Resource
    private OrganizeMapper mapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Organize record) {
        return mapper.insert(record);
    }

    @Override
    public List<Organize> listByExample(OrganizeExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public Organize getByPrimaryKey(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(Organize record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(Organize record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

}