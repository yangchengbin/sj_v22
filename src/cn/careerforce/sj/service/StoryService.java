package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.StoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class StoryService {
    @Resource
    private StoryDao storyDao;

    public List<Map<String, Object>> queryStories(int recordIndex, int pageSize) {
        List<Map<String, Object>> stories = storyDao.queryStories(recordIndex, pageSize);
        if (stories == null) {
            stories = new ArrayList<Map<String, Object>>();
        }
        return stories;
    }

    public List<Map<String, Object>> queryStoryById(String id) {
        List<Map<String, Object>> stories = storyDao.queryStoryById(id);
        if (stories == null) {
            stories = new ArrayList<Map<String, Object>>();
        }
        return stories;
    }

    public List<Map<String, Object>> queryStoriesByPid(String pid) {
        List<Map<String, Object>> stories = storyDao.queryStoriesByPid(pid);
        if (stories == null) {
            stories = new ArrayList<Map<String, Object>>();
        }
        return stories;
    }

    public List<Map<String, Object>> queryStoriesMain(int pageNumber, int pageSize) {
        return storyDao.queryStoriesMain(pageNumber, pageSize);
    }

    public List<Map<String, Object>> queryPersonStories(String userId) {
        return storyDao.queryPersonStories(userId);
    }

    public List<Map<String, Object>> queryStoryMain(String id) {
        return storyDao.queryStoryMain(id);
    }

    public void changeViewCount(String id) {
        storyDao.changeViewCount(id);
    }
}
