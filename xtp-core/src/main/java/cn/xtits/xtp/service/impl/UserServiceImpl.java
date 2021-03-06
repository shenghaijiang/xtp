
package cn.xtits.xtp.service.impl;

import cn.xtits.xtf.common.exception.XTException;
import cn.xtits.xtf.common.utils.HttpClientUtil;
import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.Dict;
import cn.xtits.xtp.entity.User;
import cn.xtits.xtp.entity.UserExample;
import cn.xtits.xtp.mapper.AppMapper;
import cn.xtits.xtp.mapper.DictMapper;
import cn.xtits.xtp.mapper.UserMapper;
import cn.xtits.xtp.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShengHaiJiang on 2017/3/7.
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${REST_XTP_URL}")
    private String REST_XTP_URL;

    @Value("${APP_TOKEN}")
    private String APP_TOKEN;

    @Resource
    private AppMapper appMapper;

    @Resource
    private DictMapper dictMapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public int deleteByPrimaryKey(Integer ID) {
        return userMapper.deleteByPrimaryKey(ID);
    }

    @Override
    public int insert(User record) {
        int count = userMapper.insert(record);
        //更新的时候数据库不同步，xtp记录误删除同步
        if (record.getAppUserId() != null && record.getAppUserId() > 0) {

        } else {
            record.setAppUserId(record.getId());
            userMapper.updateByPrimaryKey(record);
        }
        App app = appMapper.selectByPrimaryKey(record.getAppId());

        if (record.getAppUserId() != null && record.getAppUserId() > 0) {


        } else {
            Map<String, String> param = new HashMap<>();
            record.setPassword("");
            param.put("data", JsonUtil.toJson(record));
            param.put("appToken", app.getToken());
            String res = HttpClientUtil.doPost(REST_XTP_URL + "/user/updateSyncUser.json", param);
            AjaxResult r = JsonUtil.fromJson(res, AjaxResult.class);
            if (null == r || r.getCode() != 1) {
                throw new XTException(0, res);
            }
        }
        return count;
    }

    @Override
    public List<User> listByExample(UserExample example) {
        PageHelper.startPage(example.getPageIndex().intValue(), example.getPageSize().intValue());
        Page page = (Page) userMapper.selectByExample(example);
        example.setCount((int) page.getTotal());
        return page.toPageInfo().getList();
    }

    @Override
    public User getByPrimaryKey(Integer ID) {
        return userMapper.selectByPrimaryKey(ID);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        int count = userMapper.updateByPrimaryKeySelective(record);
        return count;
    }

    @Override
    public User getUserByAppUserId(Integer appUserId, Integer appId) {
        return userMapper.getUserByAppUserId(appId, appUserId);
    }

    @Override
    public List<User> listUserByRoleId(List<Integer> roleIds) {
        if (roleIds != null && roleIds.size() < 1) {
            roleIds.add(-1000);
        }
        return userMapper.listUserByRoleId(roleIds);
    }

}