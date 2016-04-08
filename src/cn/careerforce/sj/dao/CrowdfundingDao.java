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
        String sql = "SELECT id, story_id, cover_img, title, free, target_price, raised_price, support_number, begin_time, end_time, floor(( end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days, `backup`, size_img, free FROM crowdfunding WHERE story_id = " + storyId + " AND valid = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryCFDetailsByCFId(String cfId) {
        String sql = "SELECT id, name, price, number_limit_type, number_limit_count, supported_number, content, description FROM crowdfunding_detail WHERE crowdfunding_id = " + cfId + " AND valid = 1 ORDER BY id";
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> queryCFByStoryIdH5(String id) {
        String sql = "SELECT c.id, c.target_price, c.raised_price, c.support_number, floor(( end_time - UNIX_TIMESTAMP(now())) / 86400 ) AS remain_days FROM crowdfunding c WHERE c.story_id = " + id;
        List<Map<String, Object>> cfs = jdbcTemplate.queryForList(sql);
        if (cfs.size() > 0) {
            return cfs.get(0);
        }
        return null;
    }
}
