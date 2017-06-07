
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.RoleDataRule;
import cn.xtits.xtp.entity.RoleDataRuleExample;
import cn.xtits.xtp.mapper.RoleDataRuleMapper;
import cn.xtits.xtp.service.RoleDataRuleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class RoleDataRuleServiceImpl implements RoleDataRuleService {

    @Resource
    private RoleDataRuleMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(RoleDataRule record) {
        return mapper.insert(record);
    }

    @Override
    public List<RoleDataRule> listByExample(RoleDataRuleExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public RoleDataRule getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(RoleDataRule record) {
        return mapper.updateByPrimaryKey(record);
    }
}