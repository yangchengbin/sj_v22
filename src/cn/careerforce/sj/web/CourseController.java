package cn.careerforce.sj.web;

import cn.careerforce.sj.service.CourseService;
import cn.careerforce.sj.service.PersonageService;
import cn.careerforce.sj.utils.Constant;
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
 * 课程
 * Created with IntelliJ IDEA.
 * Source: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "course")
public class CourseController {
    @Resource
    private CourseService courseService;

    @Resource
    private PersonageService personageService;

    private static final Logger logger = Logger.getLogger(UserController.class);

    /**
     * 获取课程详情
     *
     * @param courseId 课程ID
     * @return
     */
    @RequestMapping(value = "queryCourseDetailInfo")
    @ResponseBody
    public Map<String, Object> queryCourseDetailInfo(String courseId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> courses = courseService.queryCourseDetailInfo(courseId);
            List<Map<String, Object>> imgs = personageService.getAllPics(courseId, 1);
            if (courses != null && courses.size() > 0) {
                obj.put("data", courses.get(0));
                obj.put("imgs", imgs);
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
            } else {
                obj.put(Constant.REQRESULT, Constant.NO_DATA);
                obj.put(Constant.MESSAGE, Constant.NO_DATA);
            }
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新课程分享量
     *
     * @param courseId 课程ID
     * @return
     */
    @RequestMapping(value = "changeShareCnt")
    @ResponseBody
    public Map<String, Object> changeShareCnt(String courseId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            courseService.changeShareCnt(courseId);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 更新课程评价量
     *
     * @param courseId 课程ID
     * @param type     操作类型
     * @return
     */
    @RequestMapping(value = "changeCommentCnt")
    @ResponseBody
    public Map<String, Object> changeCommentCnt(String courseId, @RequestParam(defaultValue = "add") String type) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            courseService.changeCommentCnt(courseId, type);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 获取课程列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "queryCourses")
    @ResponseBody
    public Map<String, Object> queryCourses(String userId, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "0") String orderType) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> courses = courseService.queryCourses(userId, pageNumber, pageSize, orderType);
            if (courses != null && courses.size() > 0) {
                obj.put("data", courses);
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
     * 更新已报名人数
     *
     * @param courseId 课程ID
     * @param type     操作类型 add:增加 reduce：减少
     * @param count    更新数量
     * @return
     */
    @RequestMapping(value = "changeEnrollmentCnt")
    @ResponseBody
    public Map<String, Object> changeEnrollmentCnt(String courseId, @RequestParam(defaultValue = "add") String type, @RequestParam(defaultValue = "1") int count) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            int affectNum = courseService.changeEnrollmentCnt(courseId, type, count);
            if (affectNum == 1) {
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
}
