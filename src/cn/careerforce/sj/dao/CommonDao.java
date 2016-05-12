package cn.careerforce.sj.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CommonDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void changeOpenCount(String objectId, String type) {
        String sql;
        if ("1".equals(type)) {//0:评论点赞 1:人物分享打开 2:故事分享打开 3:商品分享打开
            sql = "UPDATE personage SET open_count = open_count + 1 WHERE id = " + objectId;
        } else if ("2".equals(type)) {
            sql = "UPDATE story SET open_count = open_count + 1 WHERE id = " + objectId;
        } else if ("3".equals(type)) {
            sql = "UPDATE goods SET open_count = open_count + 1 WHERE id = " + objectId;
        } else {
            return;
        }
        jdbcTemplate.update(sql);
    }

    public void removeOpenCount(String objectId, String type) {
        String sql;
        if ("1".equals(type)) {//0:评论点赞 1:人物分享打开 2:故事分享打开 3:商品分享打开
            sql = "UPDATE personage SET open_count = open_count - 1 WHERE id = " + objectId;
        } else if ("2".equals(type)) {
            sql = "UPDATE story SET open_count = open_count - 1 WHERE id = " + objectId;
        } else if ("3".equals(type)) {
            sql = "UPDATE goods SET open_count = open_count - 1 WHERE id = " + objectId;
        } else {
            return;
        }
        jdbcTemplate.update(sql);
    }
}
