package com.yaolan.service.util;

/**
 *
 * httpclient返回结果处理类，可自定义
 * Created by songyj on 2015/8/31.
 */
public abstract  class Action {
    /**
     * 需要实现的动作处理
     * @see
     * @return
     */
    public abstract String doAction(Object obj);
}
