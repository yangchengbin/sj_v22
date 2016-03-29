package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.SearchDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Search: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SearchService {
    @Resource
    private SearchDao searchDao;


    public List<Map<String, Object>> queryAllKeys() {
        String sql = "SELECT `key`, type FROM retrieval WHERE valid = 1";
        return searchDao.queryAllKeys(sql);
    }
}
