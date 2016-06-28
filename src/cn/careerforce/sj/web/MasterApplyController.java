package cn.careerforce.sj.web;

import cn.careerforce.sj.model.Master;
import cn.careerforce.sj.service.MasterApplyService;
import cn.careerforce.sj.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 申请大师
 */
@Controller
@RequestMapping(value = "ma")
public class MasterApplyController {
    @Resource
    private MasterApplyService masterApplyService;

    /**
     * 新增申请
     *
     * @param master 申请人信息
     * @return
     */
    @RequestMapping(value = "addMaster")
    @ResponseBody
    public Map<String, Object> addMaster(Master master) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            masterApplyService.addMaster(master);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 查询审核状态
     *
     * @param userId 申请用户userId
     * @return
     */
    @RequestMapping(value = "qCheckStatus")
    @ResponseBody
    public Map<String, Object> qCheckStatus(String userId) {
        try {
            Map<String, Object> records = masterApplyService.qCheckStatus(userId);
            if (records.size() == 0) {
                records.put(Constant.REQRESULT, Constant.NO_DATA);
                records.put(Constant.MESSAGE, Constant.MSG_NO_DATA);
            } else {
                records.put(Constant.REQRESULT, Constant.REQSUCCESS);
                records.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
            }
            return records;
        } catch (Exception e) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
            return obj;
        }
    }

    /**
     * 查询用户申请匠人状态以及审核状态
     *
     * @param userId 申请用户ID
     * @return
     */
    @RequestMapping(value = "qMasterStatus")
    @ResponseBody
    public Map<String, Object> qMasterStatus(String userId) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            Map<String, Object> records = masterApplyService.qCheckApplyStatus(userId);
            if ("0".equals(records.get("flag").toString())) {
                obj.put(Constant.REQRESULT, Constant.NOT_APPLY);
                obj.put(Constant.MESSAGE, Constant.MSG_NOT_APPLY);
            } else {
                records = masterApplyService.qCheckStatus(userId);
                obj.put("records", records);
                obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
                obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
            }
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }

    /**
     * 根据Id获取数据
     *
     * @param id 申请ID
     * @return
     */
    @RequestMapping(value = "qMasterById")
    @ResponseBody
    public Map<String, Object> qMasterById(String id) {
        try {
            Map<String, Object> records = masterApplyService.qMasterById(id);
            if (records.get("master_apply") == null) {
                Map<String, Object> obj = new HashMap<String, Object>();
                obj.put(Constant.REQRESULT, Constant.NO_DATA);
                obj.put(Constant.MESSAGE, Constant.MSG_NO_DATA);
                return obj;
            } else {
                records.put(Constant.REQRESULT, Constant.REQSUCCESS);
                records.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
                return records;
            }
        } catch (Exception e) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
            return obj;
        }
    }


    /**
     * 新增申请
     *
     * @param master 申请人信息
     * @return
     */
    @RequestMapping(value = "updateMaster")
    @ResponseBody
    public Map<String, Object> updateMaster(Master master) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            masterApplyService.updateMaster(master);
            obj.put(Constant.REQRESULT, Constant.REQSUCCESS);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_SUCCESS);
        } catch (Exception e) {
            obj.put(Constant.REQRESULT, Constant.REQFAILED);
            obj.put(Constant.MESSAGE, Constant.MSG_REQ_FAILED);
        }
        return obj;
    }
}
