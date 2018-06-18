package com.hmall.task;

import com.hmall.common.Const;
import com.hmall.service.IOrderService;
import com.hmall.util.PropertiesUtil;
import com.hmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

import javax.annotation.PreDestroy;

/**
 * Created by @Author tachai
 * date 2018/6/11 23:21
 *
 * @Email 1206966083@qq.com
 */
@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

//
    @PreDestroy
    public void delLock(){
//        在tomcat使用shoutdown时掉用这个方法删除锁防止产生死锁
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

//    无分布式锁的
//    @Scheduled(cron = "0 */1 * * * ?")//每一分钟的整数倍执行
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrder(hour);
    }
//    分布式锁的定时任务
//    @Scheduled(cron = "0 */1 * * * ?")//每一分钟的整数倍执行
    public void cliseOrderTask2(){
        log.info("关闭订单定时任务启动");
        long lockTimeOut=Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
        if(setnxResult !=null && setnxResult.intValue() == 1){
        //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            log.info("没有获得分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务结束");
    }



    @Scheduled(cron = "0 */1 * * * ?")//每一分钟的整数倍执行
    public void cliseOrderTask3(){
        log.info("关闭订单定时任务启动");
        long lockTimeOut=Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
        if(setnxResult !=null && setnxResult.intValue() == 1){
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else {
            //未获取到锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(lockValueStr!=null&&System.currentTimeMillis()>Long.parseLong(lockValueStr)){
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeOut));
//                再次用当前时间戳getSet
                //返回给定的key的旧值 -》旧值判断，是否可以获取锁
                //当key没有旧值，即key不存在返回null ->获取锁
                //这里我们set了一个value值，获取旧的值
                if(getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr,getSetResult))){
                    //真正获得到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else {
                    log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else {
                log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }

            log.info("关闭订单定时任务结束");
        }
    }


//    设置有效期因为在内部使用
    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,5);//设置有效期防止死锁
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("==============================");
    }
}
