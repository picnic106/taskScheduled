package com.yaolan.service.dao;

import org.apache.log4j.Logger;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 *
 * 继承实际操作redis模板的dao类
 * Created by songyj on 2015/8/31.
 */
@Repository("sortedSetsDao")
public class SortedSetsDao extends RedisDao{
    Logger logger= Logger.getLogger(SortedSetsDao.class);

    /**
     * 查询有序集合的值
     * @param keyId              key名称
     * @param scoreStart        起始得分
     * @param scoreEnd          结束得分
     * @param offset            从第几个开始取
     * @param count             取多少条
     * @return
     */
   public Set<ZSetOperations.TypedTuple> getRedisData(String keyId,double scoreStart,double scoreEnd,long offset,long count){
       logger.info("keyId:"+keyId);
       try {
           //reverseRangeByScore表示根据得分从高到低查询
           //按照从高到低的方式返回值
           Set<ZSetOperations.TypedTuple> set=null;
           if(count>0){
               set=getRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(keyId, scoreStart, scoreEnd, offset, count);
           }else{
               set=getRedisTemplate().opsForZSet().reverseRangeByScoreWithScores(keyId, scoreStart, scoreEnd);
           }
           logger.info("set:"+(set==null?"null":set.size()));
           return set;
       } catch (Exception e) {
           logger.error(e.getMessage());
           return null;
       }

   }

    /**
     * 删除redis里面的得分从scoreStart到scoreEnd的值
     * @param keyId
     * @return
     */
    public long delSortedSetRedisData(String keyId,Set<Object> result){
        //删除值
        if (result!=null&&result.size()>0) {
            Object[] args = new Object[result.size()];
            int i = 0;
            for (Object obj : result) {
                args[i] = obj;
                i++;
            }
            //删除从redis查到的对象
            return delRedisData(keyId, args);

        }
        return -1;
    }

    /**
     * 删除redis里面的得分从scoreStart到scoreEnd的值
     * @param keyId
     * @return
     */
    public long delRedisData(String keyId,Object... objects){
        try {
            Long result = getRedisTemplate().opsForZSet().remove(keyId, objects);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }

    }

    public long addRedisSortedSet(String keyId,Set<ZSetOperations.TypedTuple> tt){
        try {
            Long result = getRedisTemplate().opsForZSet().add(keyId, tt);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }

    }
}