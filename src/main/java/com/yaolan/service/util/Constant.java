package com.yaolan.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Yaolan on 2015/9/11.
 */
@Component
public class Constant {
    @Value("${redis.key}")
    public   String redis_key;

    @Value("${redis.url}")
    public  String redis_url;

    @Value("${redis.count}")
    public  Long redis_count;

    @Value("${cache.suffix}")
    public  String  cache_suffix;
}
