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
public class CrowdfundingDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryCFByStoryId(String storyId) {
        String sql = "SELECT c.id, c.story_id, c.cover_img, s.title, c.free, c.target_price, c.raised_price, c.support_number, c.begin_time, c.end_time, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days, c.`backup`, c.size_img, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding c, story s WHERE s.id = c.story_id AND c.story_id = " + storyId + " AND c.valid = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryCFDetailsByCFId(String cfId) {
        String sql = "SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 1 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price > 0 AND number_limit_type = 0 AND crowdfunding_id = " + cfId + " UNION ALL SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 1 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price > 0 AND number_limit_type = 1 AND number_limit_count > supported_number AND crowdfunding_id = " + cfId + " UNION ALL SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 2 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price < 0 AND crowdfunding_id = " + cfId + " UNION ALL SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 3 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price > 0 AND number_limit_type = 1 AND number_limit_count <= supported_number AND crowdfunding_id = " + cfId + " ORDER BY seq, price";
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> queryCFByStoryIdH5(String id) {
        String sql = "SELECT c.id, c.target_price, c.raised_price, c.support_number, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days FROM crowdfunding c WHERE c.story_id = " + id;
        List<Map<String, Object>> cfs = jdbcTemplate.queryForList(sql);
        if (cfs.size() > 0) {
            return cfs.get(0);
        }
        return null;
    }

    public void changeSupportNumber(String cfdId, String type) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE crowdfunding c, crowdfunding_detail cd SET c.support_number = c.support_number + 1, cd.supported_number = cd.supported_number + 1 WHERE c.id = cd.crowdfunding_id AND cd.id = " + cfdId;
        } else {
            sql = "UPDATE crowdfunding c, crowdfunding_detail cd SET c.support_number = c.support_number - 1, cd.supported_number = cd.supported_number - 1 WHERE c.id = cd.crowdfunding_id AND cd.id = " + cfdId;
        }
        jdbcTemplate.update(sql);
    }

    public void changeRaisedPrice(String cfdId, String price) {
        String sql = "UPDATE crowdfunding c, crowdfunding_detail cd SET c.raised_price = c.raised_price + " + price + " WHERE c.id = cd.crowdfunding_id AND cd.id = " + cfdId;
        jdbcTemplate.update(sql);
    }

    public Map<String, Object> queryCFDetailById(String cfdId) {
        String sql = "SELECT cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.crowdfunding_id, cd.description, c.begin_time, c.end_time, c.cover_img FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND cd.valid = 1 AND cd.id = " + cfdId;
        return jdbcTemplate.queryForMap(sql);
    }

    public int queryCurCrowdStatus(String cfdId) {
        String sql = "SELECT IF ( c.raised_price < c.target_price, 0, 1 ) AS crowd_status FROM crowdfunding c, crowdfunding_detail cd WHERE c.id = cd.crowdfunding_id AND cd.id = " + cfdId;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public String queryAllCrowdDetailIds(String cfdId) {
        String sql = "SELECT GROUP_CONCAT(cd.id) AS id FROM crowdfunding_detail cd WHERE cd.crowdfunding_id = ( SELECT crowdfunding_id FROM crowdfunding_detail WHERE id = " + cfdId + " )";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public List<Map<String, Object>> queryPersonCrowds(String userId) {
        String sql = "SELECT c.id, c.cover_img, s.title, c.target_price, c.raised_price, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days, IF ( UNIX_TIMESTAMP() < c.begin_time, 1, IF ( UNIX_TIMESTAMP() > c.end_time, 3, 2 )) AS flag FROM crowdfunding c, story s, personage p WHERE s.id = c.story_id AND c.personage_id = p.id AND p.user_id = " + userId + " AND c.valid = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryCFMain(String id) {
        String sql = "SELECT c.id, c.cover_img, c.target_price, c.raised_price, c.support_number, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days FROM crowdfunding c WHERE c.story_id = " + id + " AND c.valid = 1";
        return jdbcTemplate.queryForList(sql);
    }
}
