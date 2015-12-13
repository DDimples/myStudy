
title: Spring MVC 结合使用Log4j2记录日志
date: 2015-10-24 13:18:02
tags: web log4j2 springMvc

---

## 背景

> 当业务飞速发展的时候，如何最快的发现异常，甚至预警异常，是系统设计不可或缺的一部分。


## Log4j2简介及使用

### 简介

1.Log4j2是Apache组织从头开发的log日志框架，不支持Log4j 1.x,官网上说性能相比1.x有很大提高，而且使用更灵活；
2.详细介绍请参考官网介绍[Apache Log4j2](http://logging.apache.org/log4j/2.x/)

### Spring 项目中使用Log4j2

1. 如上所说，使用很简单，在resource下新建[log4j2.xml](https://github.com/DDimples/myStudy/blob/master/webDemo/web/src/main/resources/log4j2.xml)资源文件
2. 在wem.xml中注册使用log4j2，

> 这种方式当程序启动的时候会自动去加载log4j2.xml，并解析装载。

	```
	<context-param>
        <param-name>isLog4jAutoInitializationDisabled</param-name>
        <param-value>true</param-value>
   	</context-param>
    ```
    
> 更多[配置方式请点击](https://logging.apache.org/log4j/2.x/manual/webapp.html)


3. 引入jar包

```
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.4.1</version>
</dependency>

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.4.1</version>
</dependency>

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-jcl</artifactId>
    <version>2.4.1</version>
</dependency>

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.4.1</version>
</dependency>
		
```

4. java程序中使用：Logger logger = LoggerFactory.getLogger(name); //均是org.slf4j包


## 日志设计

在产品中往往需要记录各种日志，比如系统提示性的日志，系统服务执行性能日志，系统运行异常日志等等；往往我们在使用的时候以这样的方式Logger logger = LoggerFactory.getLogger(****.class)，但是这样记录的日志,但是这样记录的日志不能统一输入到一个日志文件下;

下面是一号店日志类型设计的格式
![](http://pic.yupoo.com/ch1991eng/F8HsT4Yf/medish.jpg)



这个时候根据不同的业务需求，提供多个统一的logger，将相应的日志信息，按照统一的格式记录下来，后面在将对应的日志收集到Hbase、mongoDB或者MySql中，在从数据库中读取展示。这样也便于系统预警和排错，实时的展现系统性能、异常情况等等。

***代码实现***请移步[我的github](https://github.com/DDimples/myStudy/tree/master/webDemo/common/src/main/java/com/mystudy/web/common/log)

例如系统服务执行性能日志：在框架中添加一个***HandlerInterceptorAdapter*** 记录服务性能耗时及相关信息（URL、queryString、clientIp、serverIp等等）；

使用 ***AOP*** 记录servic层的性能及异常信息等等；


## 总结

当日志入口和格式统一后，对于后期分析和处理日志就更方便了。日志以统一的格式落地后，对于收集日志和日志的可视化实现则很容易实现了。这样会在系统上线后更方便的排查错误和观察系统运行情况。


> 欢迎拍砖，批评是进步的动力！ 谢谢~





