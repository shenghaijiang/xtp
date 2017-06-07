package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.UserFavorite;
import cn.xtits.xtp.entity.UserFavoriteExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface UserFavoriteService {

    int deleteByPrimaryKey(Integer ID);

    int insert(UserFavorite record);

    List<UserFavorite> listByExample(UserFavoriteExample example);

    UserFavorite getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(UserFavorite record);

    int updateUserFavorite(Integer userId, List<UserFavorite> userFavorites);
}