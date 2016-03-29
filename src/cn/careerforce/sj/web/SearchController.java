package cn.careerforce.sj.web;

import cn.careerforce.sj.service.SearchService;
import cn.careerforce.sj.utils.Constant;
import cn.careerforce.sj.utils.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    private static final Logger logger = Logger.getLogger(UserController.class);


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
            if (keys == null) {
                keys = new ArrayList<Map<String, Object>>();
            }
            obj.put("keys", keys);
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
