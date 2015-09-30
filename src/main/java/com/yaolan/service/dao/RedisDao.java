package com.yaolan.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * 实现spring redis 模板类，所有需要使用redis的dao可以继承这个类
 * 使用spring的repository注解
 * redisTemplate模板在spring配置文件里配置了
 * Created by songyj on 2015/8/31.
 */
@Repository
public abstract class RedisDao<K, V> {
    @Autowired
    private RedisTemplate<K, V> redisTemplate;

    /**
     * 设置redisTemplate
     * @param redisTemplate the redisTemplate to set
     */
    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取 RedisSerializer
     * <br>------------------------------<br>
     */
    protected RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }
}
