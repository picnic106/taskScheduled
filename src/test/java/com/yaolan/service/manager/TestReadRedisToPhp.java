package com.yaolan.service.manager;

/**
 * Created by Yaolan on 2015/9/1.
 */

import com.yaolan.service.dao.SortedSetsDao;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Yaolan on 2015/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml"})
public class TestReadRedisToPhp {
    Logger logger= Logger.getLogger(TestReadRedisToPhp.class);

    @Resource
    private ReadRedisToPhp sphp;




    @Test
    public void testSendPhp(){
        long end=1441024755;
        String keyId="expert_6cb0a11e23ca836554cbf8c64976ed5a";
        long offset=0;
        long count=2;
        sphp.service();
//        Set<Object> sets=new LinkedHashSet<>();
//        sets.add("two");
//        fa.sendPhp(sets,"myzset","httpaaa");
        //Set<Object> set=sortdao.getRedisData(keyId,0,end,0,count);
        //logger.info("set:"+set.toString());
    }

}
