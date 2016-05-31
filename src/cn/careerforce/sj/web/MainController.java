package cn.careerforce.sj.web;

import cn.careerforce.sj.service.MainService;
import cn.careerforce.sj.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * Created with IntelliJ IDEA.
 * Main: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "main")
public class MainController {
    @Resource
    private MainService mainService;

    /**
     * 发现匠人
     *
     * @param pageNumber 页码
     * @param pageSize   加载条数
     * @return
     */
    @RequestMapping(value = "queryMasters")
    @ResponseBody
    public Map<String, Object> queryMasters(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> masters = mainService.queryMasters(pageNumber, pageSize);
            if (masters != null && masters.size() > 0) {
                obj.put("masters", masters);
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
     * 手作首发
     *
     * @param pageNumber 页码
     * @param pageSize   加载条数
     * @return
     */
    @RequestMapping(value = "queryProducts")
    @ResponseBody
    public Map<String, Object> queryProducts(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> goods = mainService.queryProducts(pageNumber, pageSize);
            if (goods != null && goods.size() > 0) {
                obj.put("goods", goods);
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
     * 梦想众筹
     *
     * @param pageNumber 页码
     * @param pageSize   加载条数
     * @return
     */
    @RequestMapping(value = "queryCrowds")
    @ResponseBody
    public Map<String, Object> queryCrowds(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> crowds = mainService.queryCrowds(pageNumber, pageSize);
            if (crowds != null && crowds.size() > 0) {
                obj.put("crowds", crowds);
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
