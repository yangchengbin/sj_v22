package cn.careerforce.sj.dao;

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

    public List<Map<String, Object>> queryMasters() {
        String sql = "SELECT p.user_id, p.head_img, p.pname FROM personage p WHERE p.valid = 1 ORDER BY p.seq LIMIT 5";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryCrowds() {
        String sql = "SELECT c.id, s.title, c.cover_img, c.target_price, c.raised_price, c.support_number, p.head_img, p.pname AS master_name, p.career AS master_career, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days, IF ( UNIX_TIMESTAMP() < c.begin_time, 1, IF ( UNIX_TIMESTAMP() > c.end_time, 3, 2 )) AS flag  FROM crowdfunding c LEFT JOIN personage p ON p.id = c.personage_id LEFT JOIN story s ON s.id = c.story_id WHERE c.valid = 1 ORDER BY c.create_time DESC LIMIT 3";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryProducts(int recordIndex, int pageSize) {
        String sql = "SELECT g.id, g.cover_img, g.title, g.price FROM goods g WHERE g.valid = 1 AND g.putaway = 1 ORDER BY g.seq LIMIT " + recordIndex + " , " + pageSize;
        return jdbcTemplate.queryForList(sql);
    }
}
