package cn.careerforce.sj.utils;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.util.StrUtil;
import cn.careerforce.util.http.HttpRequest;
import net.sf.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 公共工具类
 */
public class CfCacheUtil {

    /**
     * 从公共缓存中获取用户信息
     *
     * @param userId 用户UserID
     */
    public static Map<String, String> getUserInfo(String userId) {
        String userInfoUrl = Configuration.getValue("redis_service_url") + "/api/userinfo/" + userId;
        String ret = HttpRequest.getContentByUrl(userInfoUrl, Global.default_encoding);
        Map<String, String> result = new LinkedHashMap<String, String>();
        if (StrUtil.isNotNull(ret)) {
            JSONObject jsonObject = JSONObject.fromObject(ret);
            if (jsonObject != null && jsonObject.has("result") && jsonObject.has("data") && jsonObject.get("result") != null && "0".equals(jsonObject.get("result").toString())) {
                JSONObject nicknameMap = jsonObject.getJSONObject("data");
                for (Iterator<String> i = nicknameMap.keys(); i.hasNext(); ) {
                    String key = i.next();
                    result.put(key, nicknameMap.get(key).toString());
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, String> userInfo = getUserInfo("123592540");
        System.out.println(userInfo);
    }
}
