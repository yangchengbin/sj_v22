package cn.careerforce.sj.dao;

import cn.careerforce.sj.model.Master;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class MasterApplyDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public int addMaster(Master master) {
        final String sqlSave = "INSERT INTO master_apply ( user_id, NAME, sex, head_img, id_card_positive, id_card_opposite, career, region, telephone, wx_no, work_time, personal_brand, shop_address, cooperation_platform, recommended_person, recommendation, description, create_time ) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, UNIX_TIMESTAMP())";
        final Object[] argsSave = new Object[]{master.getUserId(), master.getName(), master.getSex(), master.getHeadImg(), master.getIdCardPositive(), master.getIdCardOpposite(), master.getCareer(), master.getRegion(), master.getTelephone(), master.getWxNo(), master.getWorkTime(), master.getPersonalBrand(), master.getShopAddress(), master.getCooperationPlatform(), master.getRecommendedPerson(), master.getRecommendation(), master.getDescription()};
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < argsSave.length; i++) {
                    ps.setObject(i + 1, argsSave[i]);
                }
                return ps;
            }
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        String relatedImages = master.getRelatedImages();
        if (relatedImages != null && !"".equals(relatedImages)) {
            String[] images = relatedImages.split(",");
            if (images.length > 0) {
                String[] s = new String[images.length];
                for (int i = 0; i < images.length; i++) {
                    s[i] = "INSERT INTO rel_pictures (apply_id, img_address) VALUES (" + id + ", '" + images[i] + "')";
                }
                jdbcTemplate.batchUpdate(s);
            }
        }
        return id;
    }

    public void updateMaster(Master master) {
        //之前的图片置为无效
        String deleteImg = "UPDATE rel_pictures SET valid = 0 WHERE apply_id = " + master.getId();
        jdbcTemplate.update(deleteImg);

        //更新数据
        String updateSql = "UPDATE master_apply SET NAME = ?, sex = ?, head_img = ?, id_card_positive = ?, id_card_opposite =?, career =?, region = ?, telephone = ?, wx_no = ?, work_time = ?, personal_brand = ?, shop_address = ?, cooperation_platform = ?, recommended_person =?, recommendation = ?, description =?, check_status = 'IN', check_info = '审核中!' WHERE id = " + master.getId();
        jdbcTemplate.update(updateSql, master.getName(), master.getSex(), master.getHeadImg(), master.getIdCardPositive(), master.getIdCardOpposite(), master.getCareer(), master.getRegion(), master.getTelephone(), master.getWxNo(), master.getWorkTime(), master.getPersonalBrand(), master.getShopAddress(), master.getCooperationPlatform(), master.getRecommendedPerson(), master.getRecommendation(), master.getDescription());

        //新增图片
        String relatedImages = master.getRelatedImages();
        if (relatedImages != null && !"".equals(relatedImages)) {
            String[] images = relatedImages.split(",");
            if (images.length > 0) {
                String[] s = new String[images.length];
                for (int i = 0; i < images.length; i++) {
                    s[i] = "INSERT INTO rel_pictures (apply_id, img_address) VALUES (" + master.getId() + ", '" + images[i] + "')";
                }
                jdbcTemplate.batchUpdate(s);
            }
        }
    }
}
