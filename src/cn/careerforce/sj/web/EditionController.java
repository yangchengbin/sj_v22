package cn.careerforce.sj.web;

import cn.careerforce.sj.service.EditionService;
import cn.careerforce.sj.utils.Constant;
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
 * Edition: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "edition")
public class EditionController {
    @Resource
    private EditionService editionService;

    private static final Logger logger = Logger.getLogger(EditionController.class);

    /**
     * 获取最新版本信息
     *
     * @return
     */
    @RequestMapping(value = "queryCurEdition")
    @ResponseBody
    public Map<String, Object> queryCurEdition(@RequestParam(defaultValue = "0") int type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            Map<String, Object> edition = editionService.queryCurEdition(type);
            obj.put("edition", edition);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }

        return obj;
    }
}
