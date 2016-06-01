package cn.careerforce.sj.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Personage: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SearchDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryAllKeys(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryMasters(int type, String key, int pageNumber, int pageSize) {
        String sql = "SELECT " + type + " AS type, p.head_img, p.user_id, p.pname, p.career, EXISTS ( SELECT id FROM crowdfunding WHERE personage_id = p.id AND end_time > UNIX_TIMESTAMP() AND begin_time < UNIX_TIMESTAMP()) AS is_crowd, COUNT(g.id) + COUNT(s.id) AS rq FROM personage p LEFT JOIN goods g ON g.valid = 1 AND g.personage_id = p.id LEFT JOIN story s ON s.valid = 1 AND s.personage_id = p.id WHERE p.valid = 1 AND p.pname LIKE ? GROUP BY p.id ORDER BY rq DESC LIMIT " + (pageNumber - 1) * pageSize + ", " + pageSize;
        return jdbcTemplate.queryForList(sql, "%" + key + "%");
    }

    public List<Map<String, Object>> queryProducts(int type, String key, int pageNumber, int pageSize) {
        String sql = "SELECT  " + type + " AS type, g.id, g.cover_img, g.title, g.price FROM goods g WHERE g.valid = 1 AND g.title LIKE ? LIMIT " + (pageNumber - 1) * pageSize + ", " + pageSize;
        return jdbcTemplate.queryForList(sql, "%" + key + "%");
    }

    public List<Map<String, Object>> queryCrowdfundings(int type, String key, int pageNumber, int pageSize) {
        String sql = "SELECT  " + type + " AS type, c.id, c.cover_img, p.pname AS master_name, p.career AS master_career, c.title AS crowd_title, c.target_price, c.raised_price, c.support_number, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days FROM crowdfunding c LEFT JOIN personage p ON p.id = c.personage_id WHERE c.valid = 1 AND c.title LIKE ? LIMIT " + (pageNumber - 1) * pageSize + ", " + pageSize;
        return jdbcTemplate.queryForList(sql, "%" + key + "%");
    }

    public void addKeyHistory(String key, int type) {
        String sql = "INSERT INTO search_history (search_key, type, create_time) VALUES ('" + key + "'," + type + ", unix_timestamp(now()))";
        jdbcTemplate.update(sql);
    }
}
