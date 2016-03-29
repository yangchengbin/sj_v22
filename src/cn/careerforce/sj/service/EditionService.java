package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.EditionDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Edition: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EditionService {
    @Resource
    private EditionDao editionDao;

    public Map<String, Object> queryCurEdition(int type) {
        return editionDao.queryCurEdition(type);
    }
}
