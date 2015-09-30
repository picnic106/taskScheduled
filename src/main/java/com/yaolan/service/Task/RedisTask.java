package com.yaolan.service.Task;

import com.yaolan.service.manager.ReadRedisToPhp;
import com.yaolan.service.startup.BaseStartup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by songyj on 2015/8/31.
 *
 *
 */
@Component
public class RedisTask {

    /**
     *每500毫秒执行一次
     *
     */
    @Scheduled(fixedDelay = 200)
    void doSomethingWithDelay(){
        System.out.println(new Date()+":I'm doing with delay now!");
        ReadRedisToPhp sphp=(ReadRedisToPhp) BaseStartup.BEAN_FACTORY.getBean("readRedisToPhp");
        sphp.service();
    }
}
