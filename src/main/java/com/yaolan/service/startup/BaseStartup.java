package com.yaolan.service.startup;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * 启动类
 * 实例化spring容器
 * Created by songyj on 2015/8/31.
 */
public class BaseStartup {
    //将实例化的类放入BEAN_FACTORY
    public static ApplicationContext BEAN_FACTORY;
    Logger logger= Logger.getLogger(BaseStartup.class);

    public BaseStartup(){
        try{
            BEAN_FACTORY=new ClassPathXmlApplicationContext("spring.xml");
            logger.info("spring context init ok");
        }catch(Exception e){
            logger.error("spring context init failed");
            e.printStackTrace();
        }
    }
}
