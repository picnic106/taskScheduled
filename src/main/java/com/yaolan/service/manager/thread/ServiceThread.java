package com.yaolan.service.manager.thread;


import org.apache.log4j.Logger;

/**
 * Created by Yaolan on 2015/9/6.
 */
public class ServiceThread extends Thread {
    Logger logger= Logger.getLogger(ServiceThread.class);
    public ServiceThread(ThreadManager manager){
        this.manager = manager;
    }

    private ThreadManager manager;

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName()+",开始调用接口");
        //super.run();
        //rpService.sendPostUrl(rpService.mapResult,rpService.mapUrl);
        manager.execute();
//        try {
//            Thread.currentThread().sleep(100000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        logger.info(Thread.currentThread().getName()+",结束调用接口");
    }
}
