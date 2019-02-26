package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.AppExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface AppService {

    App getAppByToken(String token);

    App getAppByCode(String code);

    int deleteByPrimaryKey(Integer ID);

    int insert(App record);

    List<App> listByExample(AppExample example);

    App getByPrimaryKey(Integer ID);

    int updateByPrimaryKey(App record);
}