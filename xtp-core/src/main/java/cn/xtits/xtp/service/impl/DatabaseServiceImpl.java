
package cn.xtits.xtp.service.impl;

import cn.xtits.xtf.common.db.DbConn;
import cn.xtits.xtf.common.utils.PropertiesUtil;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.AppExample;
import cn.xtits.xtp.mapper.AppMapper;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.DatabaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {


    private static DbConn getConn(String connStr) throws Exception {
        DbConn conn = DbConn.createConn(connStr);
        return conn;
    }

    @Override
    public List<Map<String, Object>> listByExample(String dbName, String dbType, String connStr) {
        switch (dbType) {

            case "mysql":
                String dateSql = "select table_name tableName , IS_NULLABLE nullable ,column_name columnName , column_comment columnComment , data_type dataType , COLUMN_KEY columnKey from information_schema. columns where table_schema = ''{0}'' order by table_name , ORDINAL_POSITION";

                List<Map<String, Object>> list;
                try (DbConn conn = getConn(connStr)) {
                    String sql = MessageFormat.format(dateSql, dbName);
                    list = conn.executeList(sql);
                    return list;
                } catch (Exception e) {
                    return null;
                }
            default:
                return null;
        }

    }
}