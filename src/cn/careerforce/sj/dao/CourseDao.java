package cn.careerforce.sj.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Source: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CourseDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryCourseDetailInfo(String courseId) {
        String sql = "SELECT " +
                " c.id, " +
                " c.title, " +
                " c.category_id, " +
                " c.cover_img_detail, " +
                " c.course_time, " +
                " c.course_time_end, " +
                " c.course_address, " +
                " c.price, " +
                " c.enrollment_quantity, " +
                " c.enrolled_quantity, " +
                " c.note, " +
                " c.putaway, " +
                " c.share_count, " +
                " c.comment_count, " +
                " c.description, " +
                " p.id AS personage_id, " +
                " p.user_id AS user_id, " +
                " p.pname AS personage_name, " +
                " p.career AS personage_career, " +
                " p.head_img AS personage_head_img " +
                " FROM " +
                " course c " +
                " LEFT JOIN personage p ON p.id = c.personage_id" +
                " where c.id = " + courseId;
        return jdbcTemplate.queryForList(sql);
    }

    public void changeShareCnt(String courseId) {
        String sql = "UPDATE course SET share_count = share_count + 1 WHERE id = " + courseId;
        jdbcTemplate.update(sql);
    }

    public List<Map<String, Object>> queryCourses(String userId, int pageNumber, int pageSize, String orderType) {
        String orderBy = "";
        if ("0".equals(orderType)) {
            orderBy = " ORDER BY c.share_count DESC, c.comment_count DESC ";
        } else if ("1".equals(orderType)) {
            orderBy = " ORDER BY c.share_count DESC ";
        } else if ("2".equals(orderType)) {
            orderBy = " ORDER BY c.comment_count DESC ";
        }

        String sql = "SELECT " +
                " c.id, " +
                " c.title, " +
                " c.cover_img, " +
                " c.category_id, " +
                " c.type, " +
                " c.course_time, " +
                " c.course_time_end, " +
                " c.price, " +
                " c.share_count, " +
                " c.comment_count " +
                "FROM " +
                " course c, " +
                " personage p " +
                "WHERE " +
                " c.putaway = 1 AND c.valid = 1 AND c.personage_id = p.id AND p.user_id='" + userId + "' " + orderBy + " limit " + (pageNumber - 1) * pageSize + "," + pageSize;
        return jdbcTemplate.queryForList(sql);
    }

    public int changeEnrollmentCnt(String courseId, String type, int count) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE course SET enrolled_quantity = enrolled_quantity + " + count + " WHERE id = " + courseId + " AND enrollment_quantity >= enrolled_quantity + " + count;
        } else {
            sql = "UPDATE course SET enrolled_quantity = enrolled_quantity - " + count + " WHERE id = " + courseId;
        }
        return jdbcTemplate.update(sql);
    }

    public void changeCommentCnt(String courseId, String type) {
        String sql;
        if ("add".equals(type)) {
            sql = "UPDATE course SET comment_count = comment_count + 1 WHERE id = " + courseId;
        } else {
            sql = "UPDATE course SET comment_count = comment_count - 1 WHERE id = " + courseId;
        }
        jdbcTemplate.update(sql);
    }
}
