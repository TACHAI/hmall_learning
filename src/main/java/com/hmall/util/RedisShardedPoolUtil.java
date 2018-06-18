package com.hmall.util;

import com.hmall.common.RedisPool;
import com.hmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by @Author tachai
 * date 2018/5/27 15:15
 *
 * @Email 1206966083@qq.com
 */
@Slf4j
public class RedisShardedPoolUtil {

    //从新设置有效期
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} exTime{}  error",key,exTime,e.getMessage());
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    //session时间设置  exTime单位是秒
    public static String setEx(String key, String value,int exTime) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setEx key:{} exTime{} value:{} error",key,exTime,value,e.getMessage());
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e.getMessage());
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{}  error",key,e.getMessage());
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();;
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{}  error",key,e.getMessage());
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long setnx(String key,String value){
        ShardedJedis jedis=null;
        Long result = null;
        try{
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setnx(key,value);
        }catch (Exception e){
            log.error("set");
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
            ShardedJedis jedis = RedisShardedPool.getShardedJedis();
            RedisShardedPoolUtil.set("keyTest","value");
            String value = RedisShardedPoolUtil.get("keyTest");
            RedisShardedPoolUtil.setEx("keyex","vlueex",60*10);
            RedisShardedPoolUtil.expire("keyTest",60*20);
            RedisShardedPoolUtil.del("keyTest");
            System.out.println("end");
    }

}
