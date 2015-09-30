package com.yaolan.service.util.customerlog;

import org.apache.log4j.Level;

/**
 * Created by Yaolan on 2015/9/7.
 */
public class CustomerLevel extends Level {
    private static final long serialVersionUID = 1L;
    protected CustomerLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    /**
     * 定义log的权重为介于ERROR和FATAL之间，便于打印CUSTOMER级别的log
     */
    public static final int CUSTOMER_INT = ERROR_INT - 10;


    public static final Level CUSTOMER = new CustomerLevel(CUSTOMER_INT, "CUSTOMER", 10);

    public static Level toLevel(String logArgument) {
        if (logArgument != null && logArgument.toUpperCase().equals("CUSTOMER")) {
            return CUSTOMER;
        }
        return (Level) toLevel(logArgument);
    }

    public static Level toLevel(int val) {
        if (val == CUSTOMER_INT) {
            return CUSTOMER;
        }
        return (Level) toLevel(val, Level.DEBUG);
    }

    public static Level toLevel(int val, Level defaultLevel) {
        if (val == CUSTOMER_INT) {
            return CUSTOMER;
        }
        return Level.toLevel(val, defaultLevel);
    }

    public static Level toLevel(String logArgument, Level defaultLevel) {
        if (logArgument != null && logArgument.toUpperCase().equals("CUSTOMER")) {
            return CUSTOMER;
        }
        return Level.toLevel(logArgument, defaultLevel);
    }
}
