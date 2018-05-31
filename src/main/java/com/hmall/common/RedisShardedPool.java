package com.hmall.common;

import com.hmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @Author tachai
 * date 2018/5/31 20:00
 *
 * @Email 1206966083@qq.com
 */
public class RedisShardedPool {
    private static ShardedJedisPool pool;//jedis连接池
    private static Integer maxTatal= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")) ; //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10")) ; //最大空闲连接数
    private static Integer minIdle =Integer.parseInt(PropertiesUtil.getProperty("redis.mix.idle","2")) ;  //在jedispool中最小空闲连接jedis实例个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")) ; //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到jedis实例肯定是可用的
    private static Boolean testOnReturn =Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true")) ;  //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到jedis实例肯定是可用的

    private static String redis1Ip =PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port =Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip =PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port =Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTatal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽时，是否阻塞，false会抛出

//        //配置文件   ip  ，端口号 ，超时时间单位是毫秒
//        pool = new JedisPool(config,redisIp,redisPort,1000*2);
        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);
        List<JedisShardInfo> jedisShardInfoList=new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }
    static {
        initPool();
    }
    public  static ShardedJedis getShardedJedis(){
        return pool.getResource();
    }
    //把jedis放回连接池
    public static void returnResource(ShardedJedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }
    public static void returnBrokenResource(ShardedJedis jedis){
        if(jedis!=null){
            pool.returnBrokenResource(jedis);
        }
    }

    public static void main(String[] args) {
        ShardedJedis jedis=pool.getResource();
        for(int i=0 ;i<10;i++){
            jedis.set("key"+i,"value"+i);
        }
        returnResource(jedis);
        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }
}
