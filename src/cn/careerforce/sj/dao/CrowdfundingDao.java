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
        String sql = "SELECT c.id, c.story_id, c.cover_img, s.title, c.free, c.target_price, c.raised_price, c.support_number, c.begin_time, c.end_time, ceil(( c.end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days, c.`backup`, c.size_img FROM crowdfunding c, story s WHERE s.id = c.story_id AND c.story_id = " + storyId + " AND c.valid = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryCFDetailsByCFId(String cfId) {
        String sql = "SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 1 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price > 0 AND number_limit_type = 0 AND crowdfunding_id = " + cfId + " UNION ALL SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 1 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price > 0 AND number_limit_type = 1 AND number_limit_count > supported_number AND crowdfunding_id = " + cfId + " UNION ALL SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 2 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price < 0 AND crowdfunding_id = " + cfId + " UNION ALL SELECT cd.id, cd.`name`, cd.price, cd.number_limit_type, cd.number_limit_count, cd.supported_number, cd.content, cd.description, 3 AS seq, IF ( UNIX_TIMESTAMP(NOW()) > c.begin_time, 1, 0 ) AS is_begin FROM crowdfunding_detail cd, crowdfunding c WHERE c.id = cd.crowdfunding_id AND price > 0 AND number_limit_type = 1 AND number_limit_count <= supported_number AND crowdfunding_id = " + cfId + " ORDER BY seq, price";
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> queryCFByStoryIdH5(String id) {
        String sql = "SELECT c.id, c.target_price, c.raised_price, c.support_number, ceil(( end_time - UNIX_TIMESTAMP(now())) / 86400 ) + 1 AS remain_days FROM crowdfunding c WHERE c.story_id = " + id;
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
}
