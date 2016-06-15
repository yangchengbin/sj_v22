package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.RecordDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Record: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RecordService {
    @Resource
    private RecordDao recordDao;

    @Resource
    private CommonService commonService;

    public List<Map<String, Object>> queryRecords(int pageNumber, int pageSize) {
        return recordDao.queryRecords(pageNumber, pageSize);
    }

    public List<Map<String, Object>> queryRecordById(String id) {
        return recordDao.queryRecordById(id);
    }

    public void addShare(String id, String userId, String partner) {
        commonService.addShareRecord("record", id, userId, partner);
    }

    public void changeShareCount(String id) {
        recordDao.changeShareCount(id);
    }

    public void changeViewCount(String id) {
        recordDao.changeViewCount(id);
    }
}
