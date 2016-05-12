package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.UserDao;
import cn.careerforce.sj.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserService {
    @Resource
    private UserDao userDao;

    public List<Map<String, Object>> queryUserDetailInfo(String ticket) {
        return userDao.queryUserDetailInfo(ticket);
    }

    public void eidtUser(User user) {
        userDao.editUser(user);
    }

    public void saveBaseInfo(String userId, String regTel, String ticket, String nickName, String headImg) {
        userDao.saveBaseInfo(userId, regTel, ticket, nickName, headImg);
        /*绑定大师信息-同事务内*/
        userDao.bindMaster(userId, regTel);
    }

    public List<Map<String, Object>> queryUserDetailInfoByThird(String openId, String openType) {
        return userDao.queryUserDetailInfoByThird(openId, openType);
    }

    public List<Map<String, Object>> queryUserDetailInfoById(String userId) {
        return userDao.queryUserDetailInfoById(userId);
    }

    public void saveBaseInfoAndThirdInfo(String userId, String openId, String openType) {
        userDao.saveThirdInfo(userId, openId, openType);
    }

    public boolean existUser(String userId) {
        return userDao.existUser(userId);
    }

    public String checkForbid(String userId) {
        return userDao.checkForbid(userId);
    }

    public void addImproveInfo(String content, String contactInfo) {
        userDao.addImproveInfo(content, contactInfo);
    }
}
