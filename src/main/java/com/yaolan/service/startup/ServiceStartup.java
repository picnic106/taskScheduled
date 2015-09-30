package com.yaolan.service.startup;


import com.yaolan.service.manager.ReadRedisToPhp;

import java.util.Map;

/**
 *
 * 此方法为启动类，用于jar包启动
 * 从spring容器中取得ReadRedisToPhp bean
 * 并调用读取redis发送php接口的方法
 * Created by songyj on 2015/8/31.
 */
public class ServiceStartup extends BaseStartup{

    public static void main(String[] args) {
        ServiceStartup st=new ServiceStartup();
//        ReadRedisToPhp p=(ReadRedisToPhp)BaseStartup.BEAN_FACTORY.getBean("readRedisToPhp");
//        p.service();
//        System.out.println(Thread.currentThread().getName()+"结束了");
//        Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
//        for (Thread map:maps.keySet()){
//             System.out.println(map.getName()+";"+map.getState());
//        }
        //String ab="\u7f3a\u5c11\u5fc5\u9700\u7684\u53c2\u6570";
//        String ab="\u5f53\u524d\u64cd\u4f5c\u7981\u6b62\u7ee7\u7eed\u6267\u884c";
//        System.out.print(new String(ab.getBytes()));
    }


}
