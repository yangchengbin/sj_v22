package cn.careerforce.sj.dao;

import cn.careerforce.sj.model.User;
import cn.careerforce.sj.utils.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
@Repository
public class UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryUserDetailInfo(String ticket) {
        String sql = "SELECT id, reg_tel, ticket, nick_name, head_img, city, industry, career, age, consignee_name, consignee_tel, consignee_address, forbid FROM `user` WHERE  ticket = '" + ticket + "' AND valid = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public void editUser(User user) {
        String sql = "UPDATE USER SET nick_name = ?, city = ?, industry = ?, career = ?, age = ?, consignee_name = ?, consignee_tel = ?, consignee_address = ?, head_img = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{user.getNickName(), user.getCity(), user.getIndustry(), user.getCareer(), user.getAge(), user.getConsigneeName(), user.getConsigneeTel(), user.getConsigneeAddress(), user.getHeadImg(), user.getUserId()});
    }

    public void saveBaseInfo(String userId, String regTel, String ticket, String nickName, String headImg) {
        String sql = "insert into user (id,reg_tel,ticket,nick_name,head_img,create_time) values (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, new Object[]{userId, regTel, ticket, nickName, headImg, DateUtil.getUnixTimeStamp()});
    }

    public List<Map<String, Object>> queryUserDetailInfoByThird(String openId, String openType) {
        String sql = "SELECT " +
                " u.id, " +
                " u.reg_tel, " +
                " u.ticket, " +
                " u.nick_name, " +
                " u.head_img, " +
                " u.city, " +
                " u.industry, " +
                " u.career, " +
                " u.age, " +
                " u.consignee_name, " +
                " u.consignee_tel, " +
                " u.consignee_address, " +
                " u.forbid " +
                "FROM " +
                " third_part t, " +
                " `user` u " +
                "WHERE " +
                " u.id = t.user_id " +
                "AND t.open_id = '" + openId + "' " +
                "AND t.open_type = '" + openType + "'";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryUserDetailInfoById(String userId) {
        String sql = "SELECT id, reg_tel, ticket, nick_name, head_img, city, industry, career, age, consignee_name, consignee_tel, consignee_address, forbid FROM `user` where id = '" + userId + "'";
        return jdbcTemplate.queryForList(sql);
    }

    public void saveThirdInfo(String userId, String openId, String openType) {
        String sql = "INSERT INTO third_part (user_id, open_id, open_type) VALUES (?,?,?) ";
        jdbcTemplate.update(sql, new Object[]{userId, openId, openType});
    }

    public boolean existUser(String userId) {
        String sql = "select count(*) from user where id = '" + userId + "'";
        int userCnt = jdbcTemplate.queryForObject(sql, Integer.class);
        if (userCnt > 0) {
            return true;
        }
        return false;
    }

    public void bindMaster(String userId, String regTel) {
        String sql = "update personage set user_id = '" + userId + "' where reg_tel = '" + regTel + "'";
        jdbcTemplate.update(sql);
    }

    public String checkForbid(String userId) {
        String sql = "SELECT forbid FROM `user` WHERE id = '" + userId + "'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
