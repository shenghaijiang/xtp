package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.Log;
import cn.xtits.xtp.entity.LogExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface LogService {

    int deleteByPrimaryKey(Integer ID);

    int insert(Log record);

    List<Log> listByExample(LogExample example);

    Log getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(Log record);
}