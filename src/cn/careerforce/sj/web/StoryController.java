package cn.careerforce.sj.web;

import cn.careerforce.sj.service.StoryService;
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
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "story")
public class StoryController {
    @Resource
    private StoryService storyService;

    private static final Logger logger = Logger.getLogger(StoryController.class);

    /**
     * 获取首页故事列表
     *
     * @return
     */
    @RequestMapping(value = "queryStories")
    @ResponseBody
    public Map<String, Object> queryStories(@RequestParam(defaultValue = "0") int recordIndex, @RequestParam(defaultValue = "15") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStories(recordIndex, pageSize);
            obj.put("stories", stories);
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
     * 根据人物ID获取故事列表
     *
     * @param pid 人物Id
     * @return
     */
    @RequestMapping(value = "queryStoriesByPid")
    @ResponseBody
    public Map<String, Object> queryStoriesByPid(String pid) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoriesByPid(pid);
            obj.put("stories", stories);
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
     * 获取故事详情
     *
     * @return
     */
    @RequestMapping(value = "queryStoryById")
    @ResponseBody
    public Map<String, Object> queryStoryById(String id) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoryById(id);
            obj.put("story", stories.get(0));
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
