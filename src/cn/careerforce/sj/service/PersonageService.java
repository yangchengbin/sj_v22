package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.PersonageDao;
import cn.careerforce.sj.dao.ProcedureDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Personage: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PersonageService {
    @Resource
    private PersonageDao personageDao;

    @Resource
    private ProcedureDao procedureDao;

    public List<Map<String, Object>> queryPersonInfo(String personageId) {
        return personageDao.queryPersonInfo(personageId);
    }

    public List<Map<String, Object>> getAllPics(String personageId, int type) {
        return personageDao.getAllPics(personageId, type);
    }

    /**
     * 获取后台内定排序的人物
     *
     * @return
     */
    public List<Map<String, Object>> queryOrderPersonages(String categoryId, String recomIndex, String recomFind, int cursor, int pageSize) {
        return personageDao.queryOrderPersonages(categoryId, recomIndex, recomFind, cursor, pageSize);
    }

    public List<Map<String, Object>> queryPersonages(String categoryId, String recomIndex, String recomFind, int cursor, int pageSize, String orderType) {
        return personageDao.queryPersonages(categoryId, recomIndex, recomFind, cursor, pageSize, orderType);
    }

    public void changeAttentionCnt(String userId, String type) {
        personageDao.changeAttentionCnt(userId, type);
    }

    public void changeShareCnt(String personageId) {
        personageDao.changeShareCnt(personageId);
    }

    public String queryPersonageId(String userId) {
        return personageDao.queryPersonageId(userId);
    }

    public String queryUserId(String personageId) {
        return personageDao.queryUserId(personageId);
    }

    public List<Map<String, Object>> queryIndexCount(String categoryId) {
        return personageDao.queryIndexCount(categoryId);
    }

    public List<Map<String, Object>> queryFindCount(String categoryId) {
        return personageDao.queryFindCount(categoryId);
    }

    public void changeCommReplyCnt(String userId, String type, int count) {
        personageDao.changeCommReplyCnt(userId, type, count);
    }

    public void changeServiceOrderCnt(String userId, String type, int count) {
        personageDao.changeServiceOrderCnt(userId, type, count);
    }

    public void changeGoodsOrderCnt(String userId, String type, int count) {
        personageDao.changeGoodsOrderCnt(userId, type, count);
    }

    public List<Map<String, Object>> queryPersonsWithKey(String type, String key, int recordIndex, int pageSize) {
        String sql = "";

        if ("0".equals(type)) {
            sql = "SELECT p.id, p.user_id, p.paddr, p.pname, p.career, p.desc_video, p.video_cover_img, p.head_img, p.cover_img, p.attention_count, p.share_count, p.description, p.recommendation, p.attention_count + p.reply_comm_count + p.service_order_count + p.goods_order_count AS degree_heat FROM personage p WHERE p.valid = 1 AND p.career LIKE '%" + key + "%' ORDER BY degree_heat DESC LIMIT " + recordIndex + ", " + pageSize;
        } else if ("1".equals(type)) {
            sql = "SELECT p.id, p.user_id AS userid, p.pname AS title, p.career AS summary, p.head_img AS pictureUrl, COUNT(s.id) + count(g.id) AS degree_heat FROM personage p LEFT JOIN story s ON s.personage_id = p.id LEFT JOIN goods g ON g.personage_id = p.id WHERE p.valid = 1 AND p.pname LIKE '%" + key + "%' GROUP BY p.id ORDER BY degree_heat DESC LIMIT " + recordIndex + ", " + pageSize;
        } else if ("2".equals(type)) {
            sql = "SELECT p.id, p.user_id, p.paddr, p.pname, p.career, p.desc_video, p.video_cover_img, p.head_img, p.cover_img, p.attention_count, p.share_count, p.description, p.recommendation, p.attention_count + p.reply_comm_count + p.service_order_count + p.goods_order_count AS degree_heat,1 AS seq FROM personage p WHERE p.valid = 1 AND p.pname LIKE '%" + key + "%' UNION ALL SELECT p.id, p.user_id, p.paddr, p.pname, p.career, p.desc_video, p.video_cover_img, p.head_img, p.cover_img, p.attention_count, p.share_count, p.description, p.recommendation, p.attention_count + p.reply_comm_count + p.service_order_count + p.goods_order_count AS degree_heat,2 AS seq FROM personage p WHERE p.valid = 1 AND p.career LIKE '%" + key + "%' ORDER BY seq,degree_heat DESC  LIMIT " + recordIndex + ", " + pageSize;
        } else {
            return new ArrayList<Map<String, Object>>();
        }
        return personageDao.queryPersonsWithKey(sql);
    }

    public void addKeyHistory(String key) {
        personageDao.addKeyHistory(key);
    }

    public List<Map<String, Object>> queryPersonMain(String userId) {
        return personageDao.queryPersonMain(userId);
    }

    public List<Map<String, Object>> queryPersonMoreInfo(String userId) {
        return personageDao.queryPersonMoreInfo(userId);
    }

    public Map<String, Object> qAnchorInfo(String userId) {
        return procedureDao.callProcedureOneToMany("qAnchorInfo(?)", userId);
    }
}
