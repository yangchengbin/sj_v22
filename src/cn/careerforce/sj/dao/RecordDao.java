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
public class RecordDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryRecords(int pageNumber, int pageSize) {
        String sql = "SELECT r.id, r.type, r.title, r.record_cover, r.video_address, r.details, r.view_count FROM record r WHERE r.valid = 1 ORDER BY r.seq, r.create_time DESC LIMIT " + (pageNumber - 1) * pageSize + ", " + pageSize;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryRecordById(String id) {
        String sql = "SELECT r.id, r.type, r.title, r.record_cover, r.video_address, r.details, r.view_count FROM record r WHERE r.valid = 1 AND id = " + id;
        return jdbcTemplate.queryForList(sql);
    }

    public void changeShareCount(String id) {
        String sql = "UPDATE record SET share_count = share_count + 1 WHERE id = " + id;
        jdbcTemplate.update(sql);
    }
}
