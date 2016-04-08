package cn.careerforce.sj.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Goods: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GoodsDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryGoodsDetailInfo(String goodsId) {
        String sql = "SELECT " +
                " g.id, " +
                " g.title, " +
                " g.cover_img_detail, " +
                " g.price, " +
                " g.amount, " +
                " g.category_id, " +
                " g.note, " +
                " g.share_count, " +
                " g.comment_count, " +
                " g.putaway, " +
                " g.description, " +
                " p.id AS personage_id, " +
                " p.user_id AS user_id, " +
                " p.pname AS personage_name, " +
                " p.career AS personage_career, " +
                " p.head_img AS personage_head_img " +
                "FROM " +
                " goods g " +
                "LEFT JOIN personage p ON p.id = g.personage_id " +
                "WHERE " +
                " g.id = " + goodsId;
        return jdbcTemplate.queryForList(sql);
    }

    public void changeShareCnt(String goodsId) {
        String sql = "UPDATE goods SET share_count = share_count + 1 WHERE id = " + goodsId;
        jdbcTemplate.update(sql);
    }

    public List<Map<String, Object>> queryGoodses(String userId, int pageNumber, int pageSize, String orderType) {
        String orderBy = "";
        if ("0".equals(orderType)) {
            orderBy = " ORDER BY g.share_count DESC, g.comment_count DESC ";
        } else if ("1".equals(orderType)) {
            orderBy = " ORDER BY g.share_count DESC ";
        } else if ("2".equals(orderType)) {
            orderBy = " ORDER BY g.comment_count DESC ";
        }

        String sql = "SELECT " +
                " g.id, " +
                " g.title, " +
                " g.cover_img, " +
                " g.price, " +
                " g.category_id, " +
                " g.amount, " +
                " g.share_count, " +
                " g.comment_count " +
                "FROM " +
                " goods g, " +
                " personage p " +
                "WHERE " +
                " g.putaway = 1 AND g.valid = 1  AND g.personage_id = p.id AND p.user_id='" + userId + "' " + orderBy + " limit " + (pageNumber - 1) * pageSize + "," + pageSize;
        return jdbcTemplate.queryForList(sql);
    }

    public int changeGoodsCnt(String goodsId, String type, int count) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE goods SET amount = amount + " + count + " WHERE id = " + goodsId;
        } else {
            sql = "UPDATE goods SET amount = amount - " + count + " WHERE id = " + goodsId + " AND amount >= " + count;
        }
        return jdbcTemplate.update(sql);
    }

    public void changeCommentCnt(String goodsId, String type) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE goods SET comment_count = comment_count + 1 WHERE id = " + goodsId;
        } else {
            sql = "UPDATE goods SET comment_count = comment_count - 1 WHERE id = " + goodsId;
        }
        jdbcTemplate.update(sql);
    }

    public List<Map<String, Object>> queryGoodsByStoryId(String storyId) {
        String sql = "SELECT g.id, g.cover_img, g.title, g.price FROM goods g, story_goods sg WHERE g.id = sg.goods_id AND sg.story_id = " + storyId;
        return jdbcTemplate.queryForList(sql);
    }
}
