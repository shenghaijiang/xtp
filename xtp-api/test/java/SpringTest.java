import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.service.impl.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ShengHaiJiang on 2017/3/23.
 */
public class SpringTest {


    @Test
    public void testUpdateUser() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        User record = JsonUtil.fromJson("{             \"account\": \"测试内容12j8\",             \"QQ\": \"测试内容rh4o\",         \"groupId\": 38816,    \"mail\": \"测试内容0vk1\",             \"name\": \"测试内容i3yj\",             \"password\": 57710,             \"phone\": \"测试内容uw85\"         }", User.class);
        UserServiceImpl u = new UserServiceImpl();
        u.insert(record);
    }
}