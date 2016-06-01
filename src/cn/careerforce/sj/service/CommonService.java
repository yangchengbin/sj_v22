package cn.careerforce.sj.service;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.dao.CommonDao;
import cn.careerforce.util.http.HttpRequest;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CommonService {
    @Resource
    private CommonDao commonDao;

    public void changeOpenCount(String objectId, String type) {
        commonDao.changeOpenCount(objectId, type);
    }

    public void removeOpenCount(String objectId, String type) {
        commonDao.removeOpenCount(objectId, type);
    }

    /**
     * 获取评论信息
     *
     * @param moduleName 模块名称
     * @param objectId   对象ID
     * @param deviceNo   设备号
     * @param status     数据状态
     * @param pageNumber
     * @param pagesSize
     * @return
     */
    public JSONObject queryComments(String moduleName, String objectId, String deviceNo, int status, int pageNumber, int pagesSize) {
        String url = Configuration.getValue("feeds_service_url") + "/api/comment/query/list?clientid=123583160&module_name=" + moduleName + "&object_id=" + objectId + "&device_no=" + deviceNo + "&status=" + status + "&pageNumber=" + pageNumber + "&pagesSize=" + pagesSize;
        String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
        comment = comment.replaceAll(":null,", ":\"\",");
        if (comment != null && !comment.equals("")) {
            JSONObject commentJson = JSONObject.fromObject(comment);
            return commentJson;
        }
        return null;
    }

    /**
     * 获取关注数量
     *
     * @param moduleName 模块名称
     * @param objectId   对象ID
     * @return
     */
    public JSONObject queryAttentionCount(String moduleName, String objectId) {
        String url = Configuration.getValue("feeds_service_url") + "/api/follow/query/count?clientid=123583160&module_name=" + moduleName + "&object_id=" + objectId;
        String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
        comment = comment.replaceAll(":null,", ":\"\",");
        if (comment != null && !comment.equals("")) {
            JSONObject commentJson = JSONObject.fromObject(comment);
            return commentJson;
        }
        return null;
    }

    /**
     * 获取是否已关注
     *
     * @param moduleName 模块名称
     * @param objectId   对象ID
     * @param operateUid 操作用户ID
     * @return
     */
    public JSONObject queryHasAttention(String moduleName, String objectId, String operateUid) {
        String url = Configuration.getValue("feeds_service_url") + "/api/follow/check?clientid=123583160&module_name=" + moduleName + "&object_id=" + objectId + "&userid=" + operateUid;
        String comment = HttpRequest.getContentByUrl(url, Global.default_encoding);
        comment = comment.replaceAll(":null,", ":\"\",");
        if (comment != null && !comment.equals("")) {
            JSONObject commentJson = JSONObject.fromObject(comment);
            return commentJson;
        }
        return null;
    }

    /**
     * 添加分享记录
     *
     * @param moduleName 模块名称
     * @param objectId   对象ID
     * @param userId     分享者ID
     * @param partner    分享的第三方 可选：qq, weixin, qzone, weibo 等
     * @return
     */
    public void addShareRecord(String moduleName, String objectId, String userId, String partner) {
        String url = Configuration.getValue("feeds_service_url") + "/api/share/add?clientid=123583160&module_name=" + moduleName + "&object_id=" + objectId + "&userid=" + userId + "&partner=" + partner;
        HttpRequest.getContentByUrl(url, Global.default_encoding);
    }

    /**
     * 发现页查询人物
     *
     * @return
     */
    public List<Map<String, Object>> queryMasters() {
        return commonDao.queryMasters();
    }

    /**
     * 发现页查询众筹
     *
     * @return
     */
    public List<Map<String, Object>> queryCrowds() {
        return commonDao.queryCrowds();
    }

    /**
     * 发现页查询商品
     *
     * @return
     */
    public List<Map<String, Object>> queryProducts(int recordIndex, int pageSize) {
        return commonDao.queryProducts(recordIndex, pageSize);
    }
}
