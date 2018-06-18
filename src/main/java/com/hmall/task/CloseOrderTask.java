package com.hmall.task;

import com.hmall.common.Const;
import com.hmall.service.IOrderService;
import com.hmall.util.PropertiesUtil;
import com.hmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

//    无分布式锁的
//    @Scheduled(cron = "0 */1 * * * ?")//每一分钟的整数倍执行
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrder(hour);
    }

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

//    设置有效期因为在内部使用
    private void closeOrder(String lockName){
        RedisShardedPoolUtil.expire(lockName,50);//设置有效期防止死锁
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour"));
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("==============================");
    }
}
