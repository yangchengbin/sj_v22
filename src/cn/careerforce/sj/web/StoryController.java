package cn.careerforce.sj.web;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.service.CrowdfundingService;
import cn.careerforce.sj.service.GoodsService;
import cn.careerforce.sj.service.PersonageService;
import cn.careerforce.sj.service.StoryService;
import cn.careerforce.sj.utils.Constant;
import cn.careerforce.sj.utils.DateUtil;
import cn.careerforce.util.http.HttpRequest;
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

    @Resource
    private PersonageService personageService;

    @Resource
    private CrowdfundingService crowdfundingService;

    @Resource
    private GoodsService goodsService;

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
    public Map<String, Object> queryStoryById(String id, String userId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoryById(id);
            Map<String, Object> story = stories.get(0);

            String description = story.get("description").toString();
            if (!"".equals(description)) {
                String[] des = description.split("\\[\\$\\*\\$\\]");
                StringBuffer sb = new StringBuffer("<html><head><style type='text/css'>p{text-indent:2em;color: #333;line-height: 24px;}div {width:100%;}img {display:block; margin:10px auto;max-width:100%;}</style></head><body id='body'>");
                for (int i = 0; i < des.length; i++) {
                    if (des[i].startsWith("http")) {
                        sb.append("<div><img src='").append(des[i]).append("'/></div>");
                    } else {
                        sb.append("<p>").append(des[i]).append("</p>");
                    }
                }
                sb.append("</body></html>");
                story.put("description", sb.toString());
            }
            String personageId = story.get("personage_id").toString();
            String puId = personageService.queryUserId(personageId);  //大师的userId

            //获取评论信息
            String url = Configuration.getValue("feeds_service_url") + "/api/comment/query/list?clientid=123583160&module_name=story&object_id=" + id + "&status=0&pageNumber=1&pagesSize=2";
            String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
            comment = comment.replaceAll(":null,", ":\"\",");
            JSONObject commentJson = JSONObject.fromObject(comment);
            obj.put("comments", commentJson.get("message"));
            story.put("commentCount", commentJson.get("totalRow"));
            if (userId == null || "".equals(userId)) {
                story.put("hasAttention", 0);
            } else {
                String urlCheck = Configuration.getValue("feeds_service_url") + "/api/follow/check?clientid=123583160&module_name=figure&object_id=" + puId + "&userid=" + userId;
                String check = HttpRequest.getContentByUrl(urlCheck, Global.default_encoding);
                JSONObject jsonObject = JSONObject.fromObject(check);
                String hasAttention = jsonObject.get("message").toString();
                if ("true".equals(hasAttention)) {
                    story.put("hasAttention", 1);
                } else {
                    story.put("hasAttention", 0);
                }
            }
            obj.put("story", story);
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
     * 获取故事详情分享页面H5
     *
     * @return
     */
    @RequestMapping(value = "queryStoryByIdH5")
    @ResponseBody
    public Map<String, Object> queryStoryByIdH5(String id) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoryById(id);
            Map<String, Object> story = stories.get(0);

            String description = story.get("description").toString();
            if (!"".equals(description)) {
                String[] des = description.split("\\[\\$\\*\\$\\]");
                StringBuffer sb = new StringBuffer("<html><head><style type='text/css'>p{text-indent:2em;color: #333;line-height: 24px;}div {width:100%;}img {display:block; margin:10px auto;max-width:100%;}</style></head><body id='body'>");
                for (int i = 0; i < des.length; i++) {
                    if (des[i].startsWith("http")) {
                        sb.append("<div><img src='").append(des[i]).append("'/></div>");
                    } else {
                        sb.append("<p>").append(des[i]).append("</p>");
                    }
                }
                sb.append("</body></html>");
                story.put("description", sb.toString());
            }

            String type = story.get("type").toString(); //故事类型
            //获取评论信息
            String url = Configuration.getValue("feeds_service_url") + "/api/comment/query/list?clientid=123583160&module_name=story&object_id=" + id + "&status=0&pageNumber=1&pagesSize=2";
            String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
            comment = comment.replaceAll(":null,", ":\"\",");
            JSONObject commentJson = JSONObject.fromObject(comment);
            obj.put("comments", commentJson.get("message"));
            story.put("commentCount", commentJson.get("totalRow"));

            if ("1".equals(type)) {
                Map<String, Object> cf = crowdfundingService.queryCFByStoryIdH5(id);
                obj.put("cf", cf);
            } else {
                List<Map<String, Object>> goods = goodsService.queryGoodsByStoryIdH5(id);
                obj.put("goods", goods);
            }

            obj.put("story", story);
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
