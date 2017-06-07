package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.RoleMenuOperation;
import cn.xtits.xtp.entity.RoleMenuOperationExample;
import cn.xtits.xtp.entity.UserMenuOperation;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface RoleMenuOperationService {

    int deleteByPrimaryKey(Integer ID);

    int insert(RoleMenuOperation record);

    List<RoleMenuOperation> listByExample(RoleMenuOperationExample example);

    RoleMenuOperation getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(RoleMenuOperation record);

    int updateRoleMenuOperation(Integer roleId, List<RoleMenuOperation> roleMenuOperations);
}