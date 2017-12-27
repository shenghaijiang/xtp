package cn.xtits.xtp.sdk;

import cn.xtits.xtf.common.db.DbConn;
import cn.xtits.xtf.common.utils.PropertiesUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by HaiL on 2017/5/11.
 */
public class PermissionUtil {

    private static String operationSql = "SELECT c. CODE menuCode , b. CODE operationCode FROM menuOperation b INNER JOIN menu c ON b.MenuId = c.Id WHERE c. CODE = ''{1}'' AND b.id IN( SELECT MenuOperationId FROM roleMenuOperation WHERE RoleId IN( SELECT RoleId FROM roleUser WHERE UserId = {0}) AND menuoperationid NOT IN( SELECT menuoperationid FROM userMenuOperation WHERE userid = {0} AND type = 0) UNION ALL SELECT menuoperationid FROM userMenuOperation WHERE userid = {0} AND type = 1) and b.Code=''{2}''";

    private static String dateSql = "SELECT a.FieldName fieldName,a.Symbol symbol,a.Value value, a.RoleId roleId, a.MenuId menuId, CONCAT(a.FieldName,a.Symbol,a.Value) as criterion FROM roleDataRule a WHERE a.RoleId in(SELECT RoleId FROM roleUser WHERE UserId = {0}) and a.MenuId = {1}";

    private static String getUserIdSql = "SELECT a.id FROM user a INNER JOIN app b ON a.AppId = b.Id WHERE AppUserId = {0} AND DeleteFlag = 0 AND b.Token = ''{1}''";

    private static DbConn getConn() throws Exception {

        Properties prop = PropertiesUtil.loadProperties("xtp.properties");
        ClassLoader loder = Thread.currentThread().getContextClassLoader();
        String dbUrl = PropertiesUtil.getString(prop, "url");
        String driverClassName = PropertiesUtil.getString(prop, "driver");
        String username = PropertiesUtil.getString(prop, "username");
        String password = PropertiesUtil.getString(prop, "password");
        String connStr = MessageFormat.format("url={0}|username={1}|password={2}|driverClassName={3}|maxWait=5000", dbUrl, username, password, driverClassName);
        DbConn conn = DbConn.createConn(connStr);
        return conn;
    }

    public static Boolean IsAuthorized(String token, int userId, String menuCode, String operationCode) {

        int xtpUserId = getUserIdByAppUserId(token, userId);
        List<Map<String, Object>> list;
        try (DbConn conn = getConn()) {
            String sql = MessageFormat.format(operationSql, xtpUserId, menuCode, operationCode);
            list = conn.executeList(sql);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getMenuCode(Object o) {
        return o.getClass().getSimpleName().replace("Controller", "").toLowerCase();

    }

    public static List<Map<String, Object>> IsData(String token, int userId, Integer menuId) {

        int xtpUserId = getUserIdByAppUserId(token, userId);

        List<Map<String, Object>> list;
        try (DbConn conn = getConn()) {
            String sql = MessageFormat.format(dateSql, xtpUserId, menuId);
            list = conn.executeList(sql);
            if(list==null){
                list = new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    private static Integer getUserIdByAppUserId(String token, Integer appUserId) {

        List<Map<String, Object>> list;
        try (DbConn conn = getConn()) {
            String sql = MessageFormat.format(getUserIdSql, appUserId, token);
            list = conn.executeList(sql);
            if (list.size() > 0) {
                Integer userId = Integer.parseInt(list.get(0).get("id").toString());
                return userId;
            } else {
                return 0;
            }

        } catch (Exception e) {
            return 0;
        }
    }
}
