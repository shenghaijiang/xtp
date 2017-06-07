import cn.xtits.xtp.sdk.PermissionUtil;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by HaiL on 2017/5/11.
 */
public class PermissionTest {

    @Test
    public void Test() {
        Boolean r = PermissionUtil.IsAuthorized("eyJhbGciOiJIUzI1NiJ9NDkwMTUyNDk4LCJzd", 39, "A", "add");
        assertEquals(true, r);
    }

    @Test
    public void Test2() {
        List<Map<String, Object>> r = PermissionUtil.IsData("eyJhbGciOiJIUzI1NiJ9NDkwMTUyNDk4LCJzd", 39, "用户管理");
        assertEquals(true, r.size() > 0);
    }
}
