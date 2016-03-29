package cn.careerforce.sj.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class EditionDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> queryCurEdition(int type) {
        String sql = "SELECT id, url, version, isforce, description FROM edition where type = " + type + " ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.queryForMap(sql);
    }
}
