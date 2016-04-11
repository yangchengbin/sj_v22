package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.CrowdfundingDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Crowdfunding: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CrowdfundingService {
    @Resource
    private CrowdfundingDao crowdfundingDao;

    public List<Map<String, Object>> queryCFByStoryId(String storyId) {
        List<Map<String, Object>> cfs = crowdfundingDao.queryCFByStoryId(storyId);
        if (cfs == null) {
            cfs = new ArrayList<Map<String, Object>>();
        }
        return cfs;
    }

    public List<Map<String, Object>> queryCFDetailsByCFId(String cfId) {
        List<Map<String, Object>> cfDetails = crowdfundingDao.queryCFDetailsByCFId(cfId);
        if (cfDetails == null) {
            cfDetails = new ArrayList<Map<String, Object>>();
        }
        return cfDetails;
    }

    /*分享页面查询众筹信息*/
    public Map<String, Object> queryCFByStoryIdH5(String id) {
        return crowdfundingDao.queryCFByStoryIdH5(id);
    }

    public void changeSupportNumber(String cfdId) {
        crowdfundingDao.changeSupportNumber(cfdId);
    }

    public void changeRaisedPrice(String cfdId, String price) {
        crowdfundingDao.changeRaisedPrice(cfdId, price);
    }
}
