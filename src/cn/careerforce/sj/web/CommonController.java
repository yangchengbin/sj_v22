package cn.careerforce.sj.web;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.service.CommonService;
import cn.careerforce.sj.utils.Constant;
import cn.careerforce.sj.utils.HttpPostUtil;
import cn.careerforce.util.http.HttpRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
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
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 发现页接口
     *
     * @return
     */
    @RequestMapping(value = "find")
    @ResponseBody
    public Map<String, Object> find() {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> masters = commonService.queryMasters();   //人物
            List<Map<String, Object>> crowds = commonService.queryCrowds();     //众筹
            List<Map<String, Object>> products = commonService.queryProducts(0, 4); //商品
            obj.put("masters", masters);
            obj.put("crowds", crowds);
            obj.put("products", products);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 发现页加载商品列表
     *
     * @param recordIndex 记录索引
     * @param pageSize    加载条数
     * @return
     */
    @RequestMapping(value = "queryProductsFindPage")
    @ResponseBody
    public Map<String, Object> queryProductsFindPage(@RequestParam(defaultValue = "0") int recordIndex, @RequestParam(defaultValue = "6") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> products = commonService.queryProducts(recordIndex, pageSize); //商品
            if (products != null && products.size() > 0) {
                obj.put("products", products);
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

    /**
     * 上传到图片服务器
     *
     * @return
     */
    @RequestMapping(value = "uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam(required = false) MultipartFile[] image) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            if (image != null && image.length > 0) {
                List<String> imageUrls = HttpPostUtil.upload(image);
                obj.put("imageUrls", imageUrls);
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
