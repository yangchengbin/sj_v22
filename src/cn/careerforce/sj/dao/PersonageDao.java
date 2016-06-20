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
public class PersonageDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryPersonInfo(String personageId) {
        String sql = "SELECT id, user_id, pname, paddr, career, head_img, cover_img, cover_img_detail, desc_video, cover_img_detail AS video_cover_img, attention_count, share_count, description, LEFT(description , 100) AS sub_desc, recommendation, attention_count+reply_comm_count+service_order_count+goods_order_count AS degree_heat FROM personage WHERE id = " + personageId;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getAllPics(String personageId, int type) {
        String sql = "SELECT id, src_path AS img FROM sys_src_addr WHERE type = " + type + " AND data_id = " + personageId;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryPersonages(String categoryId, String recomIndex, String recomFind, int cursor, int pageSize, String orderType) {
        String orderBy = "";
        if ("0".equals(orderType)) {
            orderBy = " ORDER BY p.share_count DESC, p.attention_count DESC ";
        } else if ("1".equals(orderType)) {
            orderBy = " ORDER BY p.share_count DESC ";
        } else if ("2".equals(orderType)) {
            orderBy = " ORDER BY p.attention_count DESC ";
        }
        StringBuffer sql = new StringBuffer("SELECT p.id,p.user_id, p.paddr, p.pname, p.career, p.desc_video, p.video_cover_img, p.head_img, p.cover_img, p.attention_count, p.share_count, p.description, p.recommendation,p.attention_count+p.reply_comm_count+p.service_order_count+p.goods_order_count AS degree_heat FROM personage p WHERE p.valid = 1 AND p.category_id = " + categoryId + " ");

        if ("1".equals(recomIndex)) {
            sql.append(" AND p.recom_index = 1 AND p.id NOT IN ( SELECT personage_id FROM person_order where type = 1) ");
        } else if ("1".equals(recomFind)) {
            sql.append(" AND p.recom_find = 1 AND p.id NOT IN ( SELECT personage_id FROM person_order where type = 2) ");
        }
        sql.append(orderBy + "limit " + cursor + "," + pageSize);
        return jdbcTemplate.queryForList(sql.toString());
    }

    public void changeAttentionCnt(String userId, String type) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE personage SET attention_count = attention_count + 1 WHERE user_id = '" + userId + "'";
        } else {
            sql = "UPDATE personage SET attention_count = attention_count - 1 WHERE user_id = '" + userId + "'";
        }
        jdbcTemplate.update(sql);
    }

    public void changeShareCnt(String personageId) {
        String sql = "UPDATE personage SET share_count = share_count + 1 WHERE id = " + personageId;
        jdbcTemplate.update(sql);
    }

    public List<Map<String, Object>> queryOrderPersonages(String categoryId, String recomIndex, String recomFind, int cursor, int pageSize) {
        StringBuffer sql = new StringBuffer("SELECT  p.id, p.user_id, p.paddr, p.pname, p.career, p.desc_video, p.video_cover_img, p.head_img, p.cover_img, p.attention_count, p.share_count, p.description, p.recommendation, p.attention_count+p.reply_comm_count+p.service_order_count+p.goods_order_count AS degree_heat FROM personage p, person_order po WHERE p.id = po.personage_id AND p.category_id = " + categoryId + " ");

        if ("1".equals(recomIndex)) {
            sql.append(" AND po.type = 1 ");
        } else if ("1".equals(recomFind)) {
            sql.append(" AND po.type = 2 ");
        }
        sql.append(" ORDER BY po.seq limit " + cursor + ", " + pageSize);
        return jdbcTemplate.queryForList(sql.toString());
    }

    public String queryPersonageId(String userId) {
        String sql = "select id from personage where user_id = '" + userId + "'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public List<Map<String, Object>> queryIndexCount(String categoryId) {
        String sql = "SELECT count(id) AS recordCount FROM personage WHERE recom_index = 1 AND valid = 1 AND category_id = " + categoryId + " AND id NOT IN ( SELECT personage_id FROM person_order WHERE type = 1 ) UNION ALL SELECT COUNT(po.id) FROM person_order po, " +
                "personage p WHERE po.type = 1 AND po.personage_id = p.id AND p.valid = 1 AND p.category_id = " + categoryId;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryFindCount(String categoryId) {
        String sql = "SELECT count(id) AS recordCount FROM personage WHERE recom_find = 1 AND valid = 1 AND category_id = " + categoryId + " AND id NOT IN ( SELECT personage_id FROM person_order WHERE type = 2 ) UNION ALL SELECT COUNT(po.id) FROM person_order po, " +
                "personage p WHERE po.type = 2 AND po.personage_id = p.id AND p.valid = 1 AND p.category_id = " + categoryId;
        return jdbcTemplate.queryForList(sql);
    }

    public void changeCommReplyCnt(String userId, String type, int count) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE personage SET reply_comm_count = reply_comm_count + " + count + " WHERE user_id = '" + userId + "'";
        } else {
            sql = "UPDATE personage SET reply_comm_count = reply_comm_count - " + count + " WHERE user_id = '" + userId + "' AND reply_comm_count > 0";
        }
        jdbcTemplate.update(sql);
    }

    public void changeServiceOrderCnt(String userId, String type, int count) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE personage SET service_order_count = service_order_count + " + count + " WHERE user_id = '" + userId + "'";
        } else {
            sql = "UPDATE personage SET service_order_count = service_order_count - " + count + " WHERE user_id = '" + userId + "' AND service_order_count > 0";
        }
        jdbcTemplate.update(sql);
    }

    public void changeGoodsOrderCnt(String userId, String type, int count) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE personage SET goods_order_count = goods_order_count + " + count + " WHERE user_id = '" + userId + "'";
        } else {
            sql = "UPDATE personage SET goods_order_count = goods_order_count - " + count + " WHERE user_id = '" + userId + "' AND goods_order_count > 0";
        }
        jdbcTemplate.update(sql);
    }

    public List<Map<String, Object>> queryPersonsWithKey(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public String queryUserId(String personageId) {
        String sql = "select user_id from personage where id = " + personageId;
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    /*添加搜索历史记录*/
    public void addKeyHistory(String key) {
        String sql = "INSERT INTO search_history (search_key, create_time) VALUES ('" + key + "', unix_timestamp(now()))";
        jdbcTemplate.update(sql);

    }

    public List<Map<String, Object>> queryPersonMain(String userId) {
        String sql = "SELECT p.user_id, p.cover_img, p.pname, p.career, p.head_img FROM personage p WHERE p.valid = 1 AND p.user_id = " + userId;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryPersonMoreInfo(String userId) {
        String sql = "SELECT p.id, p.user_id, p.pname, p.cover_img, p.head_img, p.career, p.desc_video, p.video_cover_img, p.description FROM personage p WHERE p.user_id = '" + userId + "'";
        return jdbcTemplate.queryForList(sql);
    }
}
