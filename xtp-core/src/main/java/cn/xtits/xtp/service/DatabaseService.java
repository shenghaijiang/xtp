package cn.xtits.xtp.service;

import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.AppExample;

import java.util.List;
import java.util.Map;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
public interface DatabaseService {

    List<Map<String, Object>> listByExample(String dbName, String dbType, String url);

}