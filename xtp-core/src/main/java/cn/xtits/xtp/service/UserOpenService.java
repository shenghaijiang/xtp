package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.UserOpen;
import cn.xtits.xtp.entity.UserOpenExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2019/7/26.
 */
public interface UserOpenService {

    int insertUserOpen(UserOpen record);

    int deleteByPrimaryKey(Integer ID);

    int insert(UserOpen record);

    List<UserOpen> listByExample(UserOpenExample example);

    UserOpen getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(UserOpen record);

    int updateByPrimaryKeySelective(UserOpen record);
}