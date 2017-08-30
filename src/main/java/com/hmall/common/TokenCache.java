package com.hmall.common;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;
import sun.rmi.server.LoaderHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by asus30 on 2017/8/23.
 */
public class TokenCache {
    private static org.slf4j.Logger logger= LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX="token_";
    //第一个参数是设置缓存的初始化容量1000，后面的是缓存的最大容量10000.如果大于最大容量会使用LRU算法  这是调用链的方式所以没有先后顺序
    private static LoadingCache<String,String> localCache= CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        //默认的方法加载实现，当调用get取值的时候，如果key没有调用对应的值，就调用这个方法加载。
        @Override
        public String load(String key) throws Exception {
            return "null";
        }
    });
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getKey(String key){
        String value=null;
        try {
            value=localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }
}
