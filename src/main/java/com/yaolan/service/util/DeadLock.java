package com.yaolan.service.util;

/**
 * Created by Yaolan on 2015/9/11.
 */
public class DeadLock {

    public static void main(String[] args) {
        final Object obj_1 = new Object(), obj_2 = new Object();

        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                synchronized (obj_1) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {}

                    synchronized (obj_2) {
                        System.out.println("thread t1 done.");
                    }
                }
            }
        };

        Thread t2 = new Thread("t2"){
            @Override
            public void run() {
                synchronized (obj_2) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {}

                    synchronized (obj_1) {
                        System.out.println("thread t2 done.");
                    }
                }
            }
        };

        t1.start();
        t2.start();
    }
}
