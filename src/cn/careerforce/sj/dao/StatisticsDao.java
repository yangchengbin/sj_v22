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
public class StatisticsDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void recordStatistics() {
        String sql = "INSERT INTO statistics (create_time) VALUES (unix_timestamp(now()))";
        jdbcTemplate.update(sql);
    }
}
