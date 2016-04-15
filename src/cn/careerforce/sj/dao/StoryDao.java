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
public class StoryDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryStories(int recordIndex, int pageSize) {
        String sql = "SELECT s.id, s.title, s.type, s.cover_img, s.video_address, s.video_cover_addr, s.content, s.description, s.share_count, p.career, p.pname, p.head_img, 1 AS t, so.seq FROM story s LEFT JOIN personage p ON p.id = s.personage_id, story_order so WHERE s.valid = 1 AND s.push = 1 AND s.id = so.story_id UNION ALL SELECT s.id, s.title, s.type, s.cover_img, s.video_address, s.video_cover_addr, s.content, s.description, s.share_count, p.career, p.pname, p.head_img, 2 AS t, 9999 AS seq FROM story s LEFT JOIN personage p ON p.id = s.personage_id WHERE s.valid = 1 AND s.push = 1 AND s.id NOT IN ( SELECT story_id FROM story_order ) ORDER BY t, seq LIMIT " + recordIndex + ", " + pageSize;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryStoryById(String id) {
        String sql = "SELECT s.title, s.type, s.cover_img, s.video_address, s.video_cover_addr, s.content, s.description, s.personage_id, p.career, p.pname, p.head_img, p.attention_count, p.description AS pDesc, p.user_id, s.share_count FROM story s LEFT JOIN personage p ON p.id = s.personage_id WHERE s.id = " + id + " AND s.valid = 1";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryStoriesByPid(String pid) {
        String sql = "SELECT s.id, s.title, s.type, s.cover_img FROM story s WHERE personage_id = " + pid + " AND valid = 1 ORDER BY create_time DESC";
        return jdbcTemplate.queryForList(sql);
    }
}
