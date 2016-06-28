package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.MasterApplyDao;
import cn.careerforce.sj.dao.ProcedureDao;
import cn.careerforce.sj.model.Master;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MasterApplyService {
    @Resource
    private MasterApplyDao masterApplyDao;

    @Resource
    private ProcedureDao procedureDao;

    public int addMaster(Master master) {
        return masterApplyDao.addMaster(master);
    }

    public Map<String, Object> qCheckStatus(String userId) {
        return procedureDao.callProcedure("qCheckStatus(?)", userId);
    }

    public Map<String, Object> qMasterById(String id) {
        return procedureDao.callProcedureOneToMany("qMasterById(?)", id);
    }

    public Map<String, Object> qCheckApplyStatus(String userId) {
        return procedureDao.callProcedure("qCheckApplyStatus(?)", userId);
    }

    public void updateMaster(Master master) {
        masterApplyDao.updateMaster(master);
    }
}
