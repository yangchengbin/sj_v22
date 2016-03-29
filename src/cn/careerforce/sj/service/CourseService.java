package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.CourseDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Source: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CourseService {
    @Resource
    private CourseDao courseDao;

    public List<Map<String, Object>> queryCourseDetailInfo(String courseId) {
        return courseDao.queryCourseDetailInfo(courseId);
    }

    public void changeShareCnt(String courseId) {
        courseDao.changeShareCnt(courseId);
    }

    public List<Map<String, Object>> queryCourses(String userId, int pageNumber, int pageSize, String orderType) {
        return courseDao.queryCourses(userId, pageNumber, pageSize, orderType);
    }

    public int changeEnrollmentCnt(String courseId, String type, int count) {
        return courseDao.changeEnrollmentCnt(courseId, type, count);
    }

    public void changeCommentCnt(String courseId, String type) {
        courseDao.changeCommentCnt(courseId, type);
    }
}
