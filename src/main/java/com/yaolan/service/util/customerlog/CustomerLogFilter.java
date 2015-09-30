package com.yaolan.service.util.customerlog;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by Yaolan on 2015/9/7.
 */
public class CustomerLogFilter extends Filter {


    @Override
    public int decide(LoggingEvent lgEvent) {
        int inputLevel = lgEvent.getLevel().toInt();
        if(inputLevel>=CustomerLevel.CUSTOMER_INT && inputLevel <= CustomerLevel.CUSTOMER_INT){
            return 0;
        }

        return -1;
    }



}
