package cn.careerforce.sj.web;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.service.CommonService;
import cn.careerforce.sj.utils.Constant;
import cn.careerforce.sj.utils.DateUtil;
import cn.careerforce.util.http.HttpRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "common")
public class CommonController {
    @Resource
    private CommonService commonService;

    private static final Logger logger = Logger.getLogger(CommonController.class);

    /**
     * 添加记录
     *
     * @return
     */
    @RequestMapping(value = "addRecord")
    @ResponseBody
    public Map<String, Object> addRecord(@RequestParam("device_no") String deviceNo, @RequestParam("object_id") String objectId, @RequestParam(defaultValue = "0") String type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            String remoteUrl = Configuration.getValue("feeds_service_url") + "/api/commonRecord/add?device_no=" + deviceNo + "&object_id=" + objectId + "&type=" + type;
            String result = HttpRequest.getContentByUrl(remoteUrl, Global.default_encoding);
            if ("success".equals(result) && !"0".equals(type)) {
                commonService.changeOpenCount(objectId, type);
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
     * 移除记录
     *
     * @return
     */
    @RequestMapping(value = "removeRecord")
    @ResponseBody
    public Map<String, Object> removeRecord(@RequestParam("device_no") String deviceNo, @RequestParam("object_id") String objectId, @RequestParam(defaultValue = "0") String type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            String remoteUrl = Configuration.getValue("feeds_service_url") + "/api/commonRecord/remove?device_no=" + deviceNo + "&object_id=" + objectId + "&type=" + type;
            String result = HttpRequest.getContentByUrl(remoteUrl, Global.default_encoding);
            if ("success".equals(result) && !"0".equals(type)) {
                commonService.removeOpenCount(objectId, type);
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
}
