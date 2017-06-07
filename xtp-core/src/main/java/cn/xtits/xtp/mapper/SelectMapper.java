package cn.xtits.xtp.mapper;

import java.util.List;
import java.util.Map;

public interface SelectMapper {
    List<Map> select(String sql);

    Map get(String sql);
}