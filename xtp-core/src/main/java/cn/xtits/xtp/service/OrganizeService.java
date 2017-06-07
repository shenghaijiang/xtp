package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.Organize;
import cn.xtits.xtp.entity.OrganizeExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface OrganizeService {

    int deleteByPrimaryKey(Integer ID);

    int insert(Organize record);

    List<Organize> listByExample(OrganizeExample example);

    Organize getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(Organize record);
}