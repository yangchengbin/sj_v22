package cn.careerforce.sj.web;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.service.GoodsService;
import cn.careerforce.sj.service.PersonageService;
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
 * 商品
 * Created with IntelliJ IDEA.
 * Goods: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @Resource
    private PersonageService personageService;

    private static final Logger logger = Logger.getLogger(UserController.class);

    /**
     * 获取商品详情
     *
     * @param goodsId  商品ID
     * @param deviceNo 用户手机设备号
     * @return
     */
    @RequestMapping(value = "queryGoodsDetailInfo")
    @ResponseBody
    public Map<String, Object> queryGoodsDetailInfo(String goodsId, @RequestParam(value = "device_no", defaultValue = "0") String deviceNo) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> goods = goodsService.queryGoodsDetailInfo(goodsId);
            List<Map<String, Object>> imgs = personageService.getAllPics(goodsId, 0);

            //获取评论信息
            String url = Configuration.getValue("feeds_service_url") + "/api/comment/query/list?clientid=123583160&module_name=commodity&object_id=" + goodsId + "&device_no=" + deviceNo + "&status=0&pageNumber=1&pagesSize=2";
            String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
            comment = comment.replaceAll(":null,", ":\"\",");
            JSONObject commentJson = JSONObject.fromObject(comment);
            obj.put("comments", commentJson.get("message"));
            if (goods != null && goods.size() > 0) {
                Map<String, Object> good = goods.get(0);
                good.put("commentCount", commentJson.get("totalRow"));
                obj.put("data", good);
                obj.put("imgs", imgs);
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
     * 更新商品分享量
     *
     * @param goodsId 商品ID
     * @return
     */
    @RequestMapping(value = "changeShareCnt")
    @ResponseBody
    public Map<String, Object> changeShareCnt(String goodsId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            goodsService.changeShareCnt(goodsId);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新商品评价数量
     *
     * @param goodsId 商品ID
     * @param type    操作类型
     * @return
     */
    @RequestMapping(value = "changeCommentCnt")
    @ResponseBody
    public Map<String, Object> changeCommentCnt(String goodsId, @RequestParam(defaultValue = "add") String type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            goodsService.changeCommentCnt(goodsId, type);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 获取商品列表
     *
     * @param userId     人物userId
     * @param pageNumber 页数
     * @param pageSize   每页加载数量
     * @return
     */
    @RequestMapping(value = "queryGoodses")
    @ResponseBody
    public Map<String, Object> queryGoodses(String userId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "0") String orderType) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> goods = goodsService.queryGoodses(userId, pageNumber, pageSize, orderType);
            if (goods != null && goods.size() > 0) {
                obj.put("data", goods);
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
     * 更新商品数量
     *
     * @param goodsId 商品ID
     * @param type    操作类型
     * @return
     */
    @RequestMapping(value = "changeGoodsCnt")
    @ResponseBody
    public Map<String, Object> changeGoodsCnt(String goodsId, @RequestParam(defaultValue = "add") String type, @RequestParam(defaultValue = "1") int count) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            int[] affectNum = goodsService.changeGoodsCnt(goodsId, type, count);
            if (affectNum[0] == 1) {
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
            } else {
                obj.put(Constant.REQRESULT, Constant.REQFAILED);
                obj.put(Constant.MESSAGE, "剩余数量不足！");
            }
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 根据故事ID获取商品列表
     *
     * @param storyId 故事ID
     * @return
     */
    @RequestMapping(value = "queryGoodsByStoryId")
    @ResponseBody
    public Map<String, Object> queryGoodsByStoryId(String storyId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> goods = goodsService.queryGoodsByStoryId(storyId);
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
     * 个人主页下拉加载商品
     *
     * @param userId     人物userId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "queryPersonProducts")
    @ResponseBody
    public Map<String, Object> queryPersonProducts(String userId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> goods = goodsService.queryPersonProducts(userId, pageNumber, pageSize);
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
}
