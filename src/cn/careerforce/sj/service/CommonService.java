package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.CommonDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
