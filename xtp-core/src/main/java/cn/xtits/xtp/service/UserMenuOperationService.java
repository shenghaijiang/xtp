package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.UserMenuOperation;
import cn.xtits.xtp.entity.UserMenuOperationExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface UserMenuOperationService {

    int deleteByPrimaryKey(Integer ID);

    int insert(UserMenuOperation record);

    List<UserMenuOperation> listByExample(UserMenuOperationExample example);

    UserMenuOperation getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(UserMenuOperation record);

    int updateUserMenuOperation(Integer userId, Integer menuId, List<UserMenuOperation> userMenuOperations);
}