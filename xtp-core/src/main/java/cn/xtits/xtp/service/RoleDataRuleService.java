package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.RoleDataRule;
import cn.xtits.xtp.entity.RoleDataRuleExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface RoleDataRuleService {

    int deleteByPrimaryKey(Integer ID);

    int insert(RoleDataRule record);

    List<RoleDataRule> listByExample(RoleDataRuleExample example);

    RoleDataRule getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(RoleDataRule record);
}