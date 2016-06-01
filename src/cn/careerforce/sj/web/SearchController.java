package cn.careerforce.sj.web;

import cn.careerforce.sj.service.SearchService;
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
 * 人物
 * Created with IntelliJ IDEA.
 * Search: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "search")
public class SearchController {
    @Resource
    private SearchService searchService;

    /**
     * 检索所有关键字
     *
     * @return
     */
    @RequestMapping(value = "queryAllKeys")
    @ResponseBody
    public Map<String, Object> queryAllKeys() {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> keys = searchService.queryAllKeys();
            if (keys != null && keys.size() > 0) {
                obj.put("keys", keys);
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
     * 通用搜索功能
     *
     * @param key  关键字
     * @param type 搜索类型 0大师 1商品 2众筹
     * @return
     */
    @RequestMapping(value = "queryRecords")
    @ResponseBody
    public Map<String, Object> queryRecords(@RequestParam(required = true) String key, @RequestParam(defaultValue = "0") int type, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> records = searchService.queryRecords(key, type, pageNumber, pageSize);
            searchService.addKeyHistory(key, type);
            if (records != null && records.size() > 0) {
                obj.put("records", records);
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
