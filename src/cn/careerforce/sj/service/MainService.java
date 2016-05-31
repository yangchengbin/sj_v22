package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.MainDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Main: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MainService {
    @Resource
    private MainDao mainDao;

    public List<Map<String, Object>> queryMasters(int pageNumber, int pageSize) {
        return mainDao.queryMasters(pageNumber, pageSize);
    }

    public List<Map<String, Object>> queryProducts(int pageNumber, int pageSize) {
        return mainDao.queryProducts(pageNumber, pageSize);
    }

    public List<Map<String, Object>> queryCrowds(int pageNumber, int pageSize) {
        return mainDao.queryCrowds(pageNumber, pageSize);
    }
}
