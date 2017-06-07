package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.MenuOperation;
import cn.xtits.xtp.entity.MenuOperationExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface MenuOperationService {

    int deleteByPrimaryKey(Integer ID);

    int insert(MenuOperation record);

    List<MenuOperation> listByExample(MenuOperationExample example);

    MenuOperation getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(MenuOperation record);

    int updateByPrimaryKeySelective(MenuOperation record);
}