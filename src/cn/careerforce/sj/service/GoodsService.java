package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.GoodsDao;
import org.springframework.stereotype.Service;

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
@Service
public class GoodsService {
    @Resource
    private GoodsDao goodsDao;

    public List<Map<String, Object>> queryGoodsDetailInfo(String goodsId) {
        return goodsDao.queryGoodsDetailInfo(goodsId);
    }

    public void changeShareCnt(String goodsId) {
        goodsDao.changeShareCnt(goodsId);
    }

    public List<Map<String, Object>> queryGoodses(String userId, int pageNumber, int pageSize, String orderType) {
        return goodsDao.queryGoodses(userId, pageNumber, pageSize, orderType);
    }

    public int changeGoodsCnt(String goodsId, String type, int count) {
        return goodsDao.changeGoodsCnt(goodsId, type, count);
    }

    public void changeCommentCnt(String goodsId, String type) {
        goodsDao.changeCommentCnt(goodsId, type);
    }

    public List<Map<String, Object>> queryGoodsByStoryId(String storyId) {
        return goodsDao.queryGoodsByStoryId(storyId);
    }

    public List<Map<String, Object>> queryGoodsByStoryIdH5(String id) {
        return goodsDao.queryGoodsByStoryIdH5(id);
    }
}
