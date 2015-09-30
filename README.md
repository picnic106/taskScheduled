# taskScheduled
taskScheduled是一个使用java编写的读取redis的任务调度，每隔500毫秒读取redis里面的有序集合，并将取得的结果通过接口发送给redis.url(配置文件配置)。

#### taskScheduled技术使用

> * spring:4.1.4.RELEASE,大量使用spring注解。
> * jedis:2.7.3。
> * spring-data-redis:1.5.2.RELEASE。
> * httpclient:3.1。

1. 使用spring-redis操作有序集合：opsForZSet().reverseRangeByScoreWithScores
2. 使用spring的线程池：org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
3. 使用spring的任务调度：task:scheduled-tasks

## 使用步骤
> * 1、git clone git@github.com:picnic106/taskScheduled.git
* 2、更新 taskScheduled/src/main/resources/config.properties 中redis key信息
* 3、更新 taskScheduled/src/main/filters/(dev.properties/test.properties) 中redis host信息
* 4、本项目使用maven开发，使用之前请检查是否安装了maven
* 5、我是使用的idea开发，使用maven很方便，开发工具，请自行选择。
* 6、配置好后，通过maven打成jar包，在命令行使用java命令运行。