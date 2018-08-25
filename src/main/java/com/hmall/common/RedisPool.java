package com.hmall.common;

import com.hmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by @Author tachai
 * date 2018/5/25 16:29
 *
 * @Email 1206966083@qq.com
 */
public class RedisPool {
     private static JedisPool pool;//jedis连接池
     private static Integer maxTatal= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")) ; //最大连接数
     private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10")) ; //最大空闲连接数
     private static Integer minIdle =Integer.parseInt(PropertiesUtil.getProperty("redis.mix.idle","2")) ;  //在jedispool中最小空闲连接jedis实例个数
     private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")) ; //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到jedis实例肯定是可用的
     private static Boolean testOnReturn =Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true")) ;  //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到jedis实例肯定是可用的

    private static String redisIp =PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort =Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTatal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽时，是否阻塞，false会抛出

        //配置文件   ip  ，端口号 ，超时时间单位是毫秒
        pool = new JedisPool(config,redisIp,redisPort,1000*2);
    }
    static {
        initPool();
    }
    public  static Jedis getJedis(){
        return pool.getResource();
    }
    //把jedis放回连接池
    public static void returnResource(Jedis jedis){
        if(jedis!=null){
            pool.returnResource(jedis);
        }
    }
    public static void returnBrokenResource(Jedis jedis){
        if(jedis!=null){
            pool.returnBrokenResource(jedis);
        }
    }

    public static void main(String[] args) {
            Jedis jedis=pool.getResource();
            jedis.set("333","武器大师");
            returnResource(jedis);
            pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");
    }
}
