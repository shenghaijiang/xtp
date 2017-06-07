package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.Dict;
import cn.xtits.xtp.entity.DictExample;
import cn.xtits.xtp.entity.Log;
import cn.xtits.xtp.entity.LogExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface DictService {

    int deleteByPrimaryKey(Integer ID);

    int insert(Dict record);

    List<Dict> listByExample(DictExample example);

    Dict getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(Dict record);
}