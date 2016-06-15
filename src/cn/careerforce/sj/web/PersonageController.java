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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人物
 * Created with IntelliJ IDEA.
 * Personage: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "personage")
public class PersonageController {
    @Resource
    private PersonageService personageService;

    @Resource
    private StoryService storyService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private CrowdfundingService crowdfundingService;

    @Resource
    private CommonService commonService;

    private static final Logger logger = Logger.getLogger(UserController.class);


    /**
     * 获取人物详细信息
     *
     * @param personageId 人物ID
     * @return
     */
    @RequestMapping(value = "queryPersonInfo")
    @ResponseBody
    public Map<String, Object> queryPersonInfo(String personageId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> persons = personageService.queryPersonInfo(personageId);
            List<Map<String, Object>> imgs = personageService.getAllPics(personageId, 2);
            List<Map<String, Object>> stories = storyService.queryStoriesByPid(personageId);
            if (persons != null && persons.size() > 0) {
                obj.put("data", persons.get(0));
                obj.put("imgs", imgs);
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
     * 根据userID获取人物详细信息
     *
     * @param userId     人物ID
     * @param operateUid 当前登录户
     * @return
     */
    @RequestMapping(value = "queryPersonInfoByUserId")
    @ResponseBody
    public Map<String, Object> queryPersonInfoByUserId(String userId, String operateUid) {
        Map<String, Object> obj = new HashMap<String, Object>();
        String hasAttention;//是否关注
        try {
            String personageId = personageService.queryPersonageId(userId);
            List<Map<String, Object>> persons = personageService.queryPersonInfo(personageId);
            List<Map<String, Object>> imgs = personageService.getAllPics(personageId, 2);
            List<Map<String, Object>> stories = storyService.queryStoriesByPid(personageId);
            List<Map<String, Object>> goods = goodsService.queryGoodses(userId, 1, 9999, "0");

            if (persons != null && persons.size() > 0) {
                Map<String, Object> person = persons.get(0);

                //获取关注数量
                String attUrl = Configuration.getValue("feeds_service_url") + "/api/follow/query/count?clientid=123583160&module_name=figure&object_id=" + userId;
                String attention = HttpRequest.getContentByUrl(attUrl, Global.default_encoding);
                attention = attention.replaceAll(":null,", ":\"\",");
                JSONObject attentionJson = JSONObject.fromObject(attention);
                person.put("attention_count", attentionJson.get("message"));


                if (operateUid == null || "".equals(operateUid)) {
                    person.put("hasAttention", 0);
                } else {
                    String urlCheck = Configuration.getValue("feeds_service_url") + "/api/follow/check?clientid=123583160&module_name=figure&object_id=" + userId + "&userid=" + operateUid;
                    String check = HttpRequest.getContentByUrl(urlCheck, Global.default_encoding);
                    JSONObject jsonObject = JSONObject.fromObject(check);
                    hasAttention = jsonObject.get("message").toString();
                    if ("true".equals(hasAttention)) {
                        person.put("hasAttention", 1);
                    } else {
                        person.put("hasAttention", 0);
                    }
                }

                obj.put("data", person);
                obj.put("imgs", imgs);
                obj.put("stories", stories);
                obj.put("goods", goods);
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
     * 获取人物列表
     *
     * @param categoryId 人物分类
     * @param recomIndex 推荐到首页 0否 1是
     * @param recomFind  推荐到发现页 0否 1是
     * @param pageNumber 加载页数
     * @param pageSize   每月加载数量
     * @return
     */
    @RequestMapping(value = "queryPersonages")
    @ResponseBody
    public Map<String, Object> queryPersonages(@RequestParam(defaultValue = "1") String categoryId, @RequestParam(defaultValue = "1") String recomIndex, @RequestParam(defaultValue = "0") String recomFind, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "0") String orderType) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            int orderCount = 0; //后台人为排序大师数量
            int commonCount = 0; //非后台人为排序大师数量
            List<Map<String, Object>> countMaps = null;
            if ("1".equals(recomIndex)) {
                countMaps = personageService.queryIndexCount(categoryId);
            } else if ("1".equals(recomFind)) {
                countMaps = personageService.queryFindCount(categoryId);
            }
            if (countMaps != null) {
                commonCount = Integer.parseInt(countMaps.get(0).get("recordCount").toString());
                orderCount = Integer.parseInt(countMaps.get(1).get("recordCount").toString());
            }
            int cursor = 0;
            if (commonCount == 0 && orderCount == 0) {
                obj.put("persons", new ArrayList<Map<String, Object>>());
            } else {
                cursor = (pageNumber - 1) * pageSize;
                if (cursor < orderCount) {
                    if (cursor + pageSize <= orderCount) {
                        List<Map<String, Object>> orderPersons = personageService.queryOrderPersonages(categoryId, recomIndex, recomFind, cursor, pageSize);
                        obj.put("persons", orderPersons);
                    } else {
                        List<Map<String, Object>> orderPersons = personageService.queryOrderPersonages(categoryId, recomIndex, recomFind, cursor, orderCount - cursor);
                        List<Map<String, Object>> commonPersons = personageService.queryPersonages(categoryId, recomIndex, recomFind, 0, cursor + pageSize - orderCount, orderType);
                        orderPersons.addAll(commonPersons);
                        obj.put("persons", orderPersons);
                    }
                } else if (cursor == orderCount) {
                    List<Map<String, Object>> commonPersons = personageService.queryPersonages(categoryId, recomIndex, recomFind, 0, pageSize, orderType);
                    obj.put("persons", commonPersons);
                } else {
                    List<Map<String, Object>> commonPersons = personageService.queryPersonages(categoryId, recomIndex, recomFind, cursor - orderCount, pageSize, orderType);
                    obj.put("persons", commonPersons);
                }
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
     * 更新人物关注量
     *
     * @param userId 人物userId
     * @param type   操作类型 add添加关注cancel取消关注
     * @return
     */
    @RequestMapping(value = "changeAttentionCnt")
    @ResponseBody
    public Map<String, Object> changeAttentionCnt(String userId, @RequestParam(defaultValue = "add") String type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            personageService.changeAttentionCnt(userId, type);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新人物分享量
     *
     * @param personageId 人物ID
     * @return
     */
    @RequestMapping(value = "changeShareCnt")
    @ResponseBody
    public Map<String, Object> changeShareCnt(String personageId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            personageService.changeShareCnt(personageId);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新大师的评论回复总数
     *
     * @param userId 人物userId
     * @param type   操作类型 默认新增
     * @param count  更改数量
     * @return
     */
    @RequestMapping(value = "changeCommReplyCnt")
    @ResponseBody
    public Map<String, Object> changeCommReplyCnt(String userId, @RequestParam(defaultValue = "add") String type, @RequestParam(defaultValue = "1") int count) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            personageService.changeCommReplyCnt(userId, type, count);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新大师的服务购买次数
     *
     * @param userId 人物userId
     * @param type   操作类型 默认新增
     * @param count  更改数量
     * @return
     */
    @RequestMapping(value = "changeServiceOrderCnt")
    @ResponseBody
    public Map<String, Object> changeServiceOrderCnt(String userId, @RequestParam(defaultValue = "add") String type, @RequestParam(defaultValue = "1") int count) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            personageService.changeServiceOrderCnt(userId, type, count);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新大师的商品购买次数
     *
     * @param userId 人物userId
     * @param type   操作类型 默认新增
     * @param count  更改数量
     * @return
     */
    @RequestMapping(value = "changeGoodsOrderCnt")
    @ResponseBody
    public Map<String, Object> changeGoodsOrderCnt(String userId, @RequestParam(defaultValue = "add") String type, @RequestParam(defaultValue = "1") int count) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            personageService.changeGoodsOrderCnt(userId, type, count);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 检索大师，一、根据名称 二、根据技能
     *
     * @param type 检索类型
     * @param key  关键字
     * @return
     */
    @RequestMapping(value = "queryPersonsWithKey")
    @ResponseBody
    public Map<String, Object> queryPersonsWithKey(@RequestParam(defaultValue = "1") String type, String key, @RequestParam(defaultValue = "0") int recordIndex, @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> persons = personageService.queryPersonsWithKey(type, key, recordIndex, pageSize);
            personageService.addKeyHistory(key);
            if (persons == null) {
                persons = new ArrayList<Map<String, Object>>();
            }
            obj.put("persons", persons);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    //--------------------------------2.3版本------------------------

    /**
     * 根据userID获取人物详细信息
     *
     * @param userId     人物ID
     * @param operateUid 当前登录户
     * @return
     */
    @RequestMapping(value = "queryPersonByIdMain")
    @ResponseBody
    public Map<String, Object> queryPersonByIdMain(String userId, String operateUid) {
        Map<String, Object> obj = new HashMap<String, Object>();
        String hasAttention;//是否关注
        try {

            List<Map<String, Object>> persons = personageService.queryPersonMain(userId);
            if (persons != null && persons.size() > 0) {
                Map<String, Object> person = persons.get(0);
                List<Map<String, Object>> stories = storyService.queryPersonStories(userId);
                List<Map<String, Object>> goods = goodsService.queryPersonProducts(userId);
                List<Map<String, Object>> crowds = crowdfundingService.queryPersonCrowds(userId);

                //获取关注数
                JSONObject attentionJson = commonService.queryAttentionCount("figure", userId);
                if (attentionJson != null) {
                    person.put("attention_count", attentionJson.get("message"));
                } else {
                    person.put("attention_count", "0");
                }

                //获取是否已关注
                if (operateUid == null || "".equals(operateUid)) {
                    person.put("hasAttention", 0);
                } else {
                    JSONObject jsonObject = commonService.queryHasAttention("figure", userId, operateUid);
                    if (jsonObject != null) {
                        hasAttention = jsonObject.get("message").toString();
                        if ("true".equals(hasAttention)) {
                            person.put("hasAttention", 1);
                        } else {
                            person.put("hasAttention", 0);
                        }
                    } else {
                        person.put("hasAttention", 0);
                    }
                }
                obj.put("person", person);
                obj.put("stories", stories);
                obj.put("goods", goods);
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

    /**
     * 根据userID获取人物更多详细信息
     *
     * @param userId 人物ID
     * @return
     */
    @RequestMapping(value = "queryPersonMoreInfo")
    @ResponseBody
    public Map<String, Object> queryPersonMoreInfo(@RequestParam(required = true) String userId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> persons = personageService.queryPersonMoreInfo(userId);
            if (persons != null && persons.size() > 0) {
                Map<String, Object> person = persons.get(0);
                String description = person.get("description").toString();
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
                    person.put("description", sb.toString());
                }

                obj.put("person", person);
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
