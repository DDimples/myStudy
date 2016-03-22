title: Spring web项目中常用的annotation (二)
date: 2015-12-24 13:18:02
tags: Spring 

---


# 背景

接[上期](http://ddimples.github.io/2015/12/23/web%E9%A1%B9%E7%9B%AE%E4%B8%AD%E5%B8%B8%E7%94%A8%E7%9A%84%E6%B3%A8%E8%A7%A3/),接着总结常用的annotation。


<!--more-->

## @PostConstruct 和 @PreDestroy

这两个注解的作用为：指定的方法的生命周期过程中需要执行的操作。可以用来在启动容器或关闭容器时加载和销毁配置信息。

* @PostConstruct作用和InitializingBean接口、xml中init-method相同，在容器初始化该bean的时候执行方法；顺序为Constructor > @PostConstruct > InitializingBean > init-method
* @PreDestroy作用和DisposableBean接口、xml中的destroy-method相同，在容器关闭的时候执行该方法；




	






