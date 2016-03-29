package cn.careerforce.sj.web;

import cn.careerforce.sj.service.StatisticsService;
import cn.careerforce.sj.utils.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 用户
 * Created with IntelliJ IDEA.
 * Statistics: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "statistics")
public class StatisticsController {
    @Resource
    private StatisticsService statisticsService;

    private static final Logger logger = Logger.getLogger(StatisticsController.class);

    @RequestMapping(value = "recordStatistics")
    @ResponseBody
    public void recordStatistics() {
        try {
            statisticsService.recordStatistics();
        } catch (Exception e) {
            logger.error(DateUtil.getCurTime() + "-->" + e.getMessage());
        }
    }
}
