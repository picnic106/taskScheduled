package com.yaolan.service.manager;

import com.yaolan.service.dao.SortedSetsDao;
import com.yaolan.service.manager.thread.ServiceThread;
import com.yaolan.service.manager.thread.ThreadManager;
import com.yaolan.service.startup.BaseStartup;
import com.yaolan.service.util.Action;
import com.yaolan.service.util.Constant;
import com.yaolan.service.util.ConvertJson;
import com.yaolan.service.util.HttpClientUtils;
import com.yaolan.service.util.customerlog.CustomerLevel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 从redis读取数据并发送到php接口
 * 使用service注解，注册service成spring bean,bean id为readRedisToPhp，便于main方法读取
 * Created by songyj on 2015/9/1.
 */
@Service("readRedisToPhp")
public class ReadRedisToPhp{
    Logger logger= Logger.getLogger(ReadRedisToPhp.class);

    @Resource
    private SortedSetsDao sortedSetsDao;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${redis.key}")
    public   String redis_key;

    @Value("${redis.url}")
    public  String redis_url;

    @Value("${redis.count}")
    public  Long redis_count;

    @Value("${cache.suffix}")
    public  String  cache_suffix;

    public Map<String,Object> mapResult;
    public Map<String,Object> mapUrl;

    /**
     * 根据当前时间戳，keyid,count，
     * 从有序集合中读取数据
     *
     * sortedSetsDao.getRedisData(redis_key,0,end,0,redis_count);
     * 第二个参数为0表示从起始得分0开始读取，
     * 第四个参数为0表示从第一个值开始读取，直到count值
     * @return java集合
     */
    public Set<Object> readDelRedis(String keys){

        long currentTimeMillis=System.currentTimeMillis();

        long end=currentTimeMillis/1000;
        //long end=1441880883;

        //获取值
        Set<ZSetOperations.TypedTuple> result=sortedSetsDao.getRedisData(keys,0,end,0,redis_count);

        Set sets=null;
        //将查询出来的值放到另外一个redis
        if (result!=null&&result.size()>0) {
            long addRedisNum = sortedSetsDao.addRedisSortedSet(keys + cache_suffix, result);
            if (addRedisNum==-1) {
                logger.log(CustomerLevel.CUSTOMER, "加到缓存失败|key:" + keys + cache_suffix + "|addRedisNum:" + addRedisNum);
            }

            sets=new LinkedHashSet();
            for(ZSetOperations.TypedTuple tuple: result){
                if (tuple.getScore()>end){
                    logger.error("数据出现异常，不应该查出之后的数据:查询时间:"+end+";score:"+tuple.getScore()+";value:"+tuple.getValue());
                }
                sets.add(tuple.getValue());
            }
        }

        //将原来的key值删除掉
        if (sets!=null&&sets.size()>0) {
            long delResult = sortedSetsDao.delSortedSetRedisData(keys, sets);
            if (delResult ==-1) {
                logger.log(CustomerLevel.CUSTOMER, "删除数据失败|key:" + keys + "|result:" + result);
            }
        }

        return sets;
    }

    public void getRedisValue(){

        long currentTimeMillis=System.currentTimeMillis();
        mapResult=new HashMap<String,Object>();
        mapUrl=new HashMap<String,Object>();
        String[] keys=redis_key.split(",");
        String[] urls=redis_url.split(",");

        for (int i=0;i<keys.length;i++){
            if (keys[i]!=null&&!keys[i].equals("")
                    &&urls[i]!=null&&!urls[i].equals("")){

                Set<Object> result=readDelRedis(keys[i]);
                mapResult.put(keys[i],result);
                mapUrl.put(keys[i],urls[i]);
                //sendPhp(result, urls[i]);
            }
        }
        logger.info("从redis取值时间差："+(System.currentTimeMillis()-currentTimeMillis));

    }

    public void service(){
        long startTime=System.currentTimeMillis();
        logger.info(Thread.currentThread().getName()+",进入调度，"+startTime);
        if (redis_key!=null&&!redis_key.equals("")
                &&redis_url!=null&&!redis_url.equals("")){
            this.getRedisValue();  //读取值
            //调用spring线程池，调用接口
            //SendToInterface t=new SendToInterface();
            if (mapResult!=null&&mapResult.size()>0)
                threadPoolTaskExecutor.execute((new SendToInterface(mapResult,mapUrl,cache_suffix)));
        }
        logger.info(Thread.currentThread().getName()+"结束调度,整个服务调用时间差："+(System.currentTimeMillis()-startTime));
    }



    public static void main(String []args){
        System.out.println(new SimpleDateFormat("yyyy-MM-dd H:m:s").format(new Date())+":进来了");
    }
}
