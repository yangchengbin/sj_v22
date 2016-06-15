package cn.careerforce.sj.web;

import cn.careerforce.sj.service.CommonService;
import cn.careerforce.sj.service.RecordService;
import cn.careerforce.sj.utils.Constant;
import cn.careerforce.sj.utils.DateUtil;
import net.sf.json.JSONObject;
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
 * 记录频道
 * Created with IntelliJ IDEA.
 * Record: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "record")
public class RecordController {
    @Resource
    private RecordService recordService;

    @Resource
    private CommonService commonService;

    private static final Logger logger = Logger.getLogger(RecordController.class);

    /**
     * 查询记录列表
     *
     * @return
     */
    @RequestMapping(value = "queryRecords")
    @ResponseBody
    public Map<String, Object> queryRecords(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> records = recordService.queryRecords(pageNumber, pageSize);
            if (records.size() > 0) {
                obj.put("records", records);
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
            } else {
                obj.put(Constant.REQRESULT, Constant.NO_DATA);
                obj.put(Constant.MESSAGE, Constant.MSG_NO_DATA);
            }
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 添加视频风向记录
     *
     * @param id      视频记录ID
     * @param userId  分享用户Id
     * @param partner 分享的第三方 可选：qq, weixin, qzone, weibo 等
     * @return
     */
    @RequestMapping(value = "addShare")
    @ResponseBody
    public Map<String, Object> addShare(String id, String userId, String partner) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            recordService.addShare(id, userId, partner);
            recordService.changeShareCount(id);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 查询视频记录
     *
     * @param id       记录ID
     * @param deviceNo 设备号
     * @return
     */
    @RequestMapping(value = "queryRecordById")
    @ResponseBody
    public Map<String, Object> queryRecordById(String id, @RequestParam(defaultValue = "0") String deviceNo) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> records = recordService.queryRecordById(id);
            if (records.size() > 0) {
                Map<String, Object> record = records.get(0);
                recordService.changeViewCount(id);
                JSONObject comments = commonService.queryComments("record", id, deviceNo, 0, 1, 2);//获取评论信息
                if (comments == null) {
                    record.put("commentCount", 0);
                    obj.put("comments", "");
                } else {
                    record.put("commentCount", comments.get("totalRow"));
                    obj.put("comments", comments.get("message"));
                }
                obj.put("record", record);
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
            } else {
                obj.put(Constant.REQRESULT, Constant.NO_DATA);
                obj.put(Constant.MESSAGE, Constant.MSG_NO_DATA);
            }
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }
}
