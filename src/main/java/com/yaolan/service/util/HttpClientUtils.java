package com.yaolan.service.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * httpclient操作类
 * Created by songyj on 2015/8/31.
 */
public class HttpClientUtils {
    public static Logger logger= Logger.getLogger(HttpClientUtils.class);
    private static final String DEFAULT_CHARSET="UTF-8";//默认字符集编码
    /**
     * 执行HttpMethod对应的http请求
     * <p>
     * 注意事项
     * <p>
     * 例如<br/>
     *
     * <p>
     * @see
     * @param method
     */
    private static String doHttpMethod(HttpMethod method,Action action){
        String responseBody =null;
        HttpClient client = new HttpClient();
        try {
            client.getHttpConnectionManager().getParams().setConnectionTimeout(1000);// 设置连接超时时间
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Method failed: " + method.getStatusLine() + "URL=>" + method.getURI());
                responseBody="taskSchedules-doHttpMethod:fail";
            }else{//只有200的才解读数据包
                //Read the response body.
                responseBody = method.getResponseBodyAsString();

                logger.debug("	StatusLine=>"+method.getStatusLine()+" URL=>"+method.getURI()+"	\r\nresponseBody=>" + responseBody);
                if(responseBody!=null){
                    responseBody=action.doAction(responseBody);
                }else{
                    responseBody="taskSchedules-doHttpMethod:fail";
                }
            }
        } catch (HttpException e) {
            responseBody="taskSchedules-doHttpMethod:fail";
            logger.error("Fatal protocol violation:HttpException-"+e.getMessage());
        } catch (IOException e) {
            responseBody="taskSchedules-doHttpMethod:fail";
            logger.error("Fatal transport violation:IOException-"+e.getMessage());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return responseBody;
    }

    public static NameValuePair[] convertMaptoNamePair(Map<String,String> para){
        NameValuePair[] postData = null;
        if (para!=null){
            postData= new NameValuePair[para.size()];
            int i=0;
            for (String key:para.keySet()){
                postData[i] = new NameValuePair(key, para.get(key));
                i++;
            }
        }
        return postData;
    }

    public static String doPost(String url,Map<String,String> para,Action action){

        return doPost(url,convertMaptoNamePair(para),action);
    }

    /**
     * 执行POST的HTTP请求
     * <p>
     * 注意事项
     * <p>
     * 例如<br/>
     *
     * <p>
     * @see
     * @param url
     * @param pairs
     */
    public static String doPost(String url,NameValuePair[] pairs,Action action){
        return doPost(url, pairs, DEFAULT_CHARSET,action);
    }
    /**
     * 执行POST的HTTP请求,默认提交的编码是UTF-8
     * <p>
     * 注意事项
     * <p>
     * 例如<br/>
     *
     * <p>
     * @see
     * @param url
     * @param pairs
     * @param charset
     */
    public static String doPost(String url,NameValuePair[] pairs,String charset,Action action){
        PostMethod method=new PostMethod(url);


        if(null!=pairs){
            if(StringUtils.isEmpty(charset))
                charset=DEFAULT_CHARSET;

            if (pairs!=null) method.setRequestBody(pairs);//method.setQueryString(pairs);

            method.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset="+charset);
        }
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        return doHttpMethod(method,action);
    }


    public static String doGet(String url,Map<String,String> para,Action action){

        return doGet(url,convertMaptoNamePair(para),action);
    };

    /**
     * 执行GET的HTTP请求
     * <p>
     * 注意事项
     * <p>
     * 例如<br/>
     *
     * <p>
     * @see
     * @param url
     * @param pairs
     */
    public static String doGet(String url,NameValuePair[] pairs,Action action){

        GetMethod method=new GetMethod(url);


        if(null!=pairs){
            method.setQueryString(pairs);
        }

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        return doHttpMethod(method,action);
    }

}
