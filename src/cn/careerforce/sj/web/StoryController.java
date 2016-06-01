package cn.careerforce.sj.web;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.service.*;
import cn.careerforce.sj.utils.Constant;
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

    @Resource
    private CommonService commonService;

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
            if (stories != null && stories.size() > 0) {
                obj.put("stories", stories);
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
            if (stories != null && stories.size() > 0) {
                obj.put("stories", stories);
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
     * 获取故事详情
     *
     * @param id
     * @param userId   用户Id
     * @param deviceNo 用户设备号
     * @return
     */
    @RequestMapping(value = "queryStoryById")
    @ResponseBody
    public Map<String, Object> queryStoryById(String id, String userId, @RequestParam(value = "device_no", defaultValue = "0") String deviceNo) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoryById(id);
            Map<String, Object> story = stories.get(0);

            String description = story.get("description").toString();
            if (!"".equals(description)) {
                String[] des = description.split("\\[\\$\\*\\$\\]");
                StringBuffer sb = new StringBuffer("<html><head><style type='text/css'>body{padding:0;margin:0;}p{margin-bottom:10px;color:#585858;text-align:left;font-size:14px;line-height:24px;text-indent:2em;padding:6px 16px;}img{width:100%;margin-top:6px;}</style></head><body id='body'>");
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
            String url = Configuration.getValue("feeds_service_url") + "/api/comment/query/list?clientid=123583160&module_name=story&object_id=" + id + "&device_no=" + deviceNo + "&status=0&pageNumber=1&pagesSize=2";
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

            //获取关注数量
            String attUrl = Configuration.getValue("feeds_service_url") + "/api/follow/query/count?clientid=123583160&module_name=figure&object_id=" + puId;
            String attention = HttpRequest.getContentByUrl(attUrl, Global.default_encoding);
            attention = attention.replaceAll(":null,", ":\"\",");
            JSONObject attentionJson = JSONObject.fromObject(attention);
            story.put("attention_count", attentionJson.get("message"));

            obj.put("story", story);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 获取故事详情分享页面H5
     *
     * @param id
     * @param deviceNo 用户设备号
     * @return
     */
    @RequestMapping(value = "queryStoryByIdH5")
    @ResponseBody
    public Map<String, Object> queryStoryByIdH5(String id, @RequestParam(value = "device_no", defaultValue = "0") String deviceNo) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoryById(id);
            Map<String, Object> story = stories.get(0);

            String description = story.get("description").toString();
            if (!"".equals(description)) {
                String[] des = description.split("\\[\\$\\*\\$\\]");
                StringBuffer sb = new StringBuffer("");
                for (int i = 0; i < des.length; i++) {
                    if (des[i].startsWith("http")) {
                        sb.append("<div><img src='").append(des[i]).append("'/></div>");
                    } else {
                        sb.append("<p>").append(des[i]).append("</p>");
                    }
                }
                story.put("description", sb.toString());
            }

            String pDesc = story.get("pDesc").toString();
            if (!"".equals(pDesc)) {
                String[] des = pDesc.split("\\[\\$\\*\\$\\]");
                StringBuffer sb = new StringBuffer("");
                for (int i = 0; i < des.length; i++) {
                    if (des[i].startsWith("http")) {
                        sb.append("<div><img src='").append(des[i]).append("'/></div>");
                    } else {
                        sb.append("<p>").append(des[i]).append("</p>");
                    }
                }
                story.put("pDesc", sb.toString());
            }

            String type = story.get("type").toString(); //故事类型
            String personageId = story.get("personage_id").toString();
            String puId = personageService.queryUserId(personageId);  //大师的userId
            //获取评论信息
            String url = Configuration.getValue("feeds_service_url") + "/api/comment/query/list?clientid=123583160&module_name=story&object_id=" + id + "&device_no=" + deviceNo + "&status=0&pageNumber=1&pagesSize=2";
            String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
            comment = comment.replaceAll(":null,", ":\"\",");
            JSONObject commentJson = JSONObject.fromObject(comment);
            obj.put("comments", commentJson.get("message"));
            story.put("commentCount", commentJson.get("totalRow"));

            //获取关注数量
            String attUrl = Configuration.getValue("feeds_service_url") + "/api/follow/query/count?clientid=123583160&module_name=figure&object_id=" + puId;
            String attention = HttpRequest.getContentByUrl(attUrl, Global.default_encoding);
            attention = attention.replaceAll(":null,", ":\"\",");
            JSONObject attentionJson = JSONObject.fromObject(attention);
            story.put("attention_count", attentionJson.get("message"));

            if ("1".equals(type)) {
                Map<String, Object> cf = crowdfundingService.queryCFByStoryIdH5(id);
                obj.put("cf", cf);
            } else {
                List<Map<String, Object>> goods = goodsService.queryGoodsByStoryIdH5(id);
                obj.put("goods", goods);
            }

            obj.put("story", story);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    //------------------------------2.3版本---------------------------

    /**
     * 获取首页故事列表
     *
     * @param pageNumber 页码
     * @param pageSize   加载条数
     * @return
     */
    @RequestMapping(value = "queryStoriesMain")
    @ResponseBody
    public Map<String, Object> queryStoriesMain(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoriesMain(pageNumber, pageSize);
            if (stories != null && stories.size() > 0) {
                obj.put("stories", stories);
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
     * 获取故事详情
     *
     * @param id       故事id
     * @param deviceNo 用户设备号
     * @return
     */
    @RequestMapping(value = "queryStoryMain")
    @ResponseBody
    public Map<String, Object> queryStoryMain(String id, @RequestParam(value = "device_no", defaultValue = "0") String deviceNo) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> stories = storyService.queryStoryMain(id);
            if (stories != null && stories.size() > 0) {
                Map<String, Object> story = stories.get(0);
                //-------------装配详情字段-------------
                String description = story.get("description").toString();
                if (!"".equals(description)) {
                    String[] des = description.split("\\[\\$\\*\\$\\]");
                    StringBuffer sb = new StringBuffer("<html><head><style type='text/css'>body{padding:0;margin:0;}p{margin-bottom:10px;color:#585858;text-align:left;font-size:14px;line-height:24px;text-indent:2em;padding:6px 16px;}img{width:100%;margin-top:6px;}</style></head><body id='body'>");
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
                //---------------获取评论信息----------------------
                JSONObject commentJson = commonService.queryComments("story", id, deviceNo, 0, 1, 2);
                if (commentJson != null) {
                    story.put("commentCount", commentJson.get("totalRow"));
                    obj.put("comments", commentJson.get("message"));
                } else {
                    story.put("commentCount", 0);
                    obj.put("comments", null);
                }
                obj.put("story", story);

                String type = story.get("type").toString();//故事类型 1众筹 2商品 3纯故事
                if ("1".equals(type)) {
                    List<Map<String, Object>> crowds = crowdfundingService.queryCFMain(id);
                    if (crowds != null && crowds.size() > 0) {
                        obj.put("crowd", crowds.get(0));
                    } else {
                        obj.put("crowd", null);
                    }
                } else if ("2".equals(type)) {
                    List<Map<String, Object>> products = goodsService.queryProductsByStoryId(id);
                    if (products != null && products.size() > 0) {
                        obj.put("product", products);
                    } else {
                        obj.put("product", null);
                    }
                }
                //更新阅读量
                storyService.changeViewCount(id);
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
