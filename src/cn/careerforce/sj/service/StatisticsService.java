package cn.careerforce.sj.service;

import cn.careerforce.sj.dao.StatisticsDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Statistics: nanmeiying
 * Date: 15-10-29
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */
@Service
public class StatisticsService {
    @Resource
    private StatisticsDao statisticsDao;

    public void recordStatistics() {
        statisticsDao.recordStatistics();
    }
}
