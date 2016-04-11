package cn.careerforce.sj.web;

import cn.careerforce.sj.service.CrowdfundingService;
import cn.careerforce.sj.utils.Constant;
import cn.careerforce.sj.utils.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 * Created with IntelliJ IDEA.
 * Crowdfunding: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "crowdfunding")
public class CrowdfundingController {
    @Resource
    private CrowdfundingService crowdfundingService;

    private static final Logger logger = Logger.getLogger(CrowdfundingController.class);

    /**
     * 根据故事ID获取众筹信息
     *
     * @param storyId
     * @return
     */
    @RequestMapping(value = "queryCFByStoryId")
    @ResponseBody
    public Map<String, Object> queryCFByStoryId(String storyId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> cfs = crowdfundingService.queryCFByStoryId(storyId);
            if (cfs.size() > 0) {
                obj.put("cf", cfs.get(0));
                List<Map<String, Object>> cfDetails = crowdfundingService.queryCFDetailsByCFId(cfs.get(0).get("id").toString());
                obj.put("cfDetails", cfDetails);
            }
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, "操作成功");
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, "操作失败");
        }
        return obj;
    }

    /**
     * 根据众筹明细ID更改众筹支持人数
     *
     * @param cfdId 众筹明细ID
     * @return
     */
    @RequestMapping(value = "changeSupportNumber")
    @ResponseBody
    public Map<String, Object> changeSupportNumber(String cfdId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            crowdfundingService.changeSupportNumber(cfdId);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, "操作成功");
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, "操作失败");
        }
        return obj;
    }

    /**
     * 根据众筹明细ID更改众筹已筹金额
     *
     * @param cfdId 众筹明细ID
     * @param price 众筹明细金额
     * @return
     */
    @RequestMapping(value = "changeRaisedPrice")
    @ResponseBody
    public Map<String, Object> changeRaisedPrice(String cfdId, @RequestParam(defaultValue = "0") String price) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            crowdfundingService.changeRaisedPrice(cfdId, price);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, "操作成功");
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, "操作失败");
        }
        return obj;
    }
}
