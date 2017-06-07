
package cn.xtits.xtp.service.impl;

import cn.xtits.xtp.entity.Log;
import cn.xtits.xtp.entity.LogExample;
import cn.xtits.xtp.mapper.LogMapper;
import cn.xtits.xtp.service.LogService;
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
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper mapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return mapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(Log record) {
        return mapper.insert(record);
    }

    @Override
    public List<Log> listByExample(LogExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) mapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public Log getByPrimaryKey(Integer ID) {
        return mapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(Log record) {
        return mapper.updateByPrimaryKey(record);
    }
}