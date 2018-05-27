package com.hmall.util;

import com.hmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by @Author tachai
 * date 2018/5/27 16:32
 *
 * @Email 1206966083@qq.com
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper=new ObjectMapper();
    static {
        //对象所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日其格式同意为    yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略在json字符串中存在但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj:objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            log.warn("Parse object to string error",e);
            return null;
        }
    }
    //返回封装好的json字符串
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }catch (Exception e){
            log.warn("Parse object to string error",e);
            return null;
        }
    }


    //字符串转换成对象
    public static <T> T string2obj(String str,TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str)|| typeReference ==null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str:objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static <T> T string2obj(String str,Class<?> collectionClass,Class<?>... elementclasses){
        JavaType javaType= objectMapper.getTypeFactory().constructParametricType(collectionClass,elementclasses);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    public static void main(String[] args) {
            User u1=new User();
            u1.setId(8888);
            u1.setEmail("1206966083@qq.com");
            String userJson = JsonUtil.obj2String(u1);
            String userJsonPreety = JsonUtil.obj2StringPretty(u1);
            log.info("user1Json:{}",userJson);
            log.info("userJsonPreety:{}",userJsonPreety);

            User user =JsonUtil.string2obj(userJson, new TypeReference<User>() {});
            System.out.println("end");
    }

}
