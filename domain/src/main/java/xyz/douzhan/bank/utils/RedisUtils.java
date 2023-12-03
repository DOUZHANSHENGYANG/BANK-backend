package xyz.douzhan.bank.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 一些声明信息
 * Description: redis工具配置类
 * date: 2023/12/2 20:11
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class RedisUtils {
    private static RedisTemplate<String,Object> redisTemplate;

   public RedisUtils(RedisConnectionFactory redisConnectionFactory){
       RedisUtils.redisTemplate=getRedisTemplate(redisConnectionFactory);
   }

    /**
     * 自定义RedisTemplate
     * @param redisConnectionFactory
     * @return
     */

    public RedisTemplate<String,Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(createObjectMapper(), Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();


        return redisTemplate;
    }
    private ObjectMapper createObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY
        );
        return mapper;
    }


    /**
     * 设置
     *
     * @param key
     * @param val
     */
    public static boolean set(String key, Object val) {
        if (StrUtil.isEmpty(key)){
            return false;
        }
        redisTemplate.opsForValue().set(key, val);
        return true;
    }

    /**
     * 设置附带失效时间
     *
     * @param key
     * @param val
     * @param time
     * @param timeUnit
     * @return
     */
    public static boolean setWithExpire(String key, Object val, long time, TimeUnit timeUnit) {
        if (time <= 0) {
            return false;
        }
        redisTemplate.opsForValue().set(key, val, time, timeUnit);
        return true;
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 判断是否存在key
     *
     * @param key
     * @return
     */
    public static boolean has(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     *
     * @param key
     */
    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }


}

