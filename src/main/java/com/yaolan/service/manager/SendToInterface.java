package com.yaolan.service.manager;

import com.yaolan.service.dao.SortedSetsDao;
import com.yaolan.service.manager.thread.ThreadManager;
import com.yaolan.service.startup.BaseStartup;
import com.yaolan.service.util.Action;
import com.yaolan.service.util.Constant;
import com.yaolan.service.util.ConvertJson;
import com.yaolan.service.util.HttpClientUtils;
import com.yaolan.service.util.customerlog.CustomerLevel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Yaolan on 2015/9/8.
 */
public class SendToInterface extends Thread {
    Logger logger= Logger.getLogger(SendToInterface.class);


    public SendToInterface(Map<String,Object>  mapResult,Map<String, Object> mapUrl,String cache_suffix){
        this.mapResult=mapResult;
        this.mapUrl=mapUrl;
        this.cache_suffix=cache_suffix;
    }

    private SortedSetsDao sortedSetsDao;
    private String cache_suffix;

    public Map<String,Object>  mapResult;

    public Map<String, Object> getMapResult() {
        return mapResult;
    }

    public void setMapResult(Map<String, Object> mapResult) {
        this.mapResult = mapResult;
    }

    public Map<String, Object> getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(Map<String, Object> mapUrl) {
        this.mapUrl = mapUrl;
    }

    Map<String,Object> mapUrl;

    @Override
    public void run(){
        long startTime=System.currentTimeMillis();
        if (mapResult!=null&&mapResult.size()>0) {
            for (String key : mapResult.keySet()) {
                Set<Object> sets = (Set<Object>) mapResult.get(key);
                sendPhp(sets,key, (String) mapUrl.get(key));
            }
        }
        logger.info("发送接口时间差："+(System.currentTimeMillis()-startTime));

    }

    /**
     * 将从redis读出的数据发送给php接口
     * 先将redis数据组织成json
     */
    public void sendPhp(Set<Object> result,String key,String urls){
        Constant con=new Constant();
        //long startTime=System.currentTimeMillis();
        //logger.info("startTime："+startTime);
        //Set<Object> result=readDelRedis();
        //将redis数据组织成json
        //String json= ConvertJson.set2json(result);

        //logger.info("json："+json);

        //将json串当作参数


        if (result!=null&&result.size()>0){
            int i=0;
            for (Object results:result){
                Map para=new HashMap<String, String>();
                para.put("data",results);
                //发送http post请求
                String httpResult =null;
                try {
                     httpResult = HttpClientUtils.doPost(urls, para, new Action() {
                        public String doAction(Object obj) {
                            return obj == null ? null : obj.toString();
                        }
                    });
                }catch (Exception e){
                    httpResult="taskSchedules-doHttpMethod:fail";
                    logger.info("发送接口失败:"+e.getMessage());
                }
                //logger.error(i+"接口接收到的数据:" + (new String(httpResult.getBytes())));

                if (httpResult==null||httpResult.indexOf("taskSchedules-doHttpMethod:fail")!=-1){
                    //logger.error("发送接口失败了,发送数据json:" + json);
                    //CustomerLog.customerLog(logger,"发送接口失败了,发送数据json:" + json);
                    logger.log(CustomerLevel.CUSTOMER,i+"发送接口失败|key:"+key+"|result:"+results);
                }else{
                    sortedSetsDao=(SortedSetsDao)BaseStartup.BEAN_FACTORY.getBean("sortedSetsDao");
                    logger.info(i+"发送接口成功："+httpResult);
                    //发送成功后,将缓存值删掉
                    //if (i<=0) {
                        long delResult = sortedSetsDao.delRedisData(key + cache_suffix, results);
                   // }
                }
                i++;
            }
        }



       // String httpResult="sucess";



        //long endTime=System.currentTimeMillis();
        //logger.info("endTime："+endTime);

        //logger.info("发送接口时间差："+(endTime-startTime));

    }
}
