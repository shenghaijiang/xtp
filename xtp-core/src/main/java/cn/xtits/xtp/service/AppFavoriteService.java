package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.AppFavorite;
import cn.xtits.xtp.entity.AppFavoriteExample;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface AppFavoriteService {

    int deleteByPrimaryKey(Integer ID);

    int insert(AppFavorite record);

    List<AppFavorite> listByExample(AppFavoriteExample example);

    AppFavorite getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(AppFavorite record);

    int updateAppFavorite(Integer appId, String menuIds);
}