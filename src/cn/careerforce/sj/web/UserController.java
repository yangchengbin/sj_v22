package cn.careerforce.sj.web;

import cn.careerforce.config.Configuration;
import cn.careerforce.config.Global;
import cn.careerforce.sj.model.User;
import cn.careerforce.sj.service.UserService;
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
import java.io.IOException;
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
@RequestMapping(value = "user")
public class UserController {
    @Resource
    private UserService userService;

    private static final Logger logger = Logger.getLogger(UserController.class);

    /**
     * 发送手机验证码
     *
     * @param regTel 注册手机号
     * @return
     */
    @RequestMapping(value = "sendSmsCode")
    @ResponseBody
    public Map<String, Object> sendSmsCode(String regTel, @RequestParam(defaultValue = "1") String sq, @RequestParam(defaultValue = "0") String func) {
        Map<String, Object> obj = new HashMap<String, Object>();
        String mobile = regTel;

        if (!"".equals(mobile)) {
            StringBuffer url = new StringBuffer(512);
            url.append(Configuration.getValue("sso_url"));
            url.append("api/v2/sso/validatecode?action=getValidateCode&clientid=");
            url.append(Configuration.getValue("open_clientid"));
            url.append("&token=");
            url.append(Configuration.getValue("open_token"));
            url.append("&sn=");
            url.append(Configuration.getValue("sms_sn"));
            url.append("&sw=");
            url.append(Configuration.getValue("sms_sw"));
            url.append("&sq=");
            url.append(sq);
            url.append("&sp={{code}}&sc=");
            url.append(Configuration.getValue("sms_sc"));
            url.append("&mobile=");
            url.append(mobile);
            url.append("&func=");
            url.append(func);
            String str = HttpRequest.getContentByUrl(url.toString(), Global.default_encoding);
            JSONObject jsonObject = JSONObject.fromObject(str);
            obj.put(Constant.REQRESULT, jsonObject.get("result"));
            Object msg = jsonObject.get("message");
            if ("null".equals(msg.toString())) {
                obj.put(Constant.MESSAGE, "");
            } else {
                obj.put(Constant.MESSAGE, msg);
            }
        }
        return obj;
    }

    /**
     * 根据用户ID获取用户详细资料
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "queryUserDetailInfoById")
    @ResponseBody
    public Map<String, Object> queryUserDetailInfoById(String userId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> users = userService.queryUserDetailInfoById(userId);
            if (users != null && users.size() > 0) {
                obj.put("data", users.get(0));
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, "操作成功");
            } else {
                obj.put(Constant.REQRESULT, Constant.NOUSER);
                obj.put(Constant.MESSAGE, "用户不存在");
            }
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, "操作失败");
        }
        return obj;
    }

    /**
     * 获取用户详细资料 暂不需要
     *
     * @param ticket
     * @return
     */
    @RequestMapping(value = "queryUserDetailInfo")
    @ResponseBody
    public Map<String, Object> queryUserDetailInfo(String ticket) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> user = userService.queryUserDetailInfo(ticket);
            if (user.size() > 0) {
                obj.put("data", user.get(0));
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, "操作成功");
            } else {
                obj.put(Constant.REQRESULT, Constant.NOUSER);
                obj.put(Constant.MESSAGE, "用户不存在");
            }
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, "操作失败");
        }
        return obj;
    }

    /**
     * 根据第三方账号获取用户详细资料      暂不需要
     *
     * @param openId   第三方账号
     * @param openType 第三方类型 qq、weixin、xinlang
     * @return
     */
    @RequestMapping(value = "queryUserDetailInfoByThird")
    @ResponseBody
    public Map<String, Object> queryUserDetailInfoByThird(String openId, String openType) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> user = userService.queryUserDetailInfoByThird(openId, openType);
            if (user.size() > 0) {
                obj.put("data", user.get(0));
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, "操作成功");
            } else {
                obj.put(Constant.REQRESULT, Constant.NOUSER);
                obj.put(Constant.MESSAGE, "用户不存在");
            }
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, "操作失败");
        }
        return obj;
    }

    /**
     * 保存用户基本信息
     *
     * @param userId   sso返回的用户ID
     * @param regTel   注册手机号
     * @param ticket   保存凭据
     * @param nickName 昵称
     * @return
     */
    @RequestMapping(value = "saveBaseInfo")
    @ResponseBody
    public Map<String, Object> saveBaseInfo(String userId, String regTel, String ticket, String nickName, String headImg) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            userService.saveBaseInfo(userId, regTel, ticket, nickName, headImg);
            //将用户信息存入缓存中
            String url = Configuration.getValue("redis_service_url") + "/api/master/put/" + userId;
            HttpRequest.getContentByUrl(url, Global.default_encoding);

            List<Map<String, Object>> users = userService.queryUserDetailInfoById(userId);
            obj.put("data", users.get(0));
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
     * 通过第三方注册用户   暂不需要
     *
     * @param userId   用户Id
     * @param regTel   注册手机号
     * @param ticket   凭据
     * @param openId   第三方ID
     * @param openType 第三方类型
     * @param nickName 昵称
     * @return
     */
    @RequestMapping(value = "saveBaseInfoByThird")
    @ResponseBody
    public Map<String, Object> saveBaseInfoByThird(String userId, String regTel, String ticket, String openId, String openType, String nickName, String headImg) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            if (!userService.existUser(userId)) {
                userService.saveBaseInfo(userId, regTel, ticket, nickName, headImg);
            }
            userService.saveBaseInfoAndThirdInfo(userId, openId, openType);
            List<Map<String, Object>> users = userService.queryUserDetailInfoById(userId);
            obj.put("data", users.get(0));
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
     * 编辑用户资料
     *
     * @param user 用户
     * @return
     */
    @RequestMapping(value = "editUser")
    @ResponseBody
    public Map<String, Object> editUser(User user) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            userService.eidtUser(user);
            //更新缓存中的用户信息
            String url = Configuration.getValue("redis_service_url") + "/api/master/put/" + user.getUserId();
            HttpRequest.getContentByUrl(url, Global.default_encoding);

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
     * 查询用户是否禁言
     *
     * @param userId 用户ID
     * @return
     */
    @RequestMapping(value = "checkForbid")
    @ResponseBody
    public String checkForbid(String userId) {
        try {
            return userService.checkForbid(userId);
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 重启tomcat服务
     *
     * @return
     */
    @RequestMapping(value = "restartTomcat")
    @ResponseBody
    public Map<String, Object> restartTomcat(@RequestParam(defaultValue = "1") String type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        String tomcatRoot = System.getProperty("user.dir");
        String shutdown = tomcatRoot + "/shutdown.sh";//shutdown.bat
        String startup = tomcatRoot + "/startup.sh";//startup.bat
        try {
            if ("0".equals(type)) {
                Runtime.getRuntime().exec(shutdown);
                Runtime.getRuntime().exec(startup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        obj.put("tomcatRoot", tomcatRoot);
        obj.put("shutdown", shutdown);
        obj.put("startup", startup);
        return obj;
    }
}
