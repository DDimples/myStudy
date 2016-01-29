# rabbitMQ使用
[TOC]

## 安装
1. [rabbitMQ首页](http://www.rabbitmq.com/) 可以在上面下载对应的安装版本；
2. 进入[install页面](http://www.rabbitmq.com/install-rpm.html)  
>安装rabbitMQ前需要安装Erlang包，根据**Installation using repository**的步骤安装Erlang；

### 命令行
``` linux
## Adding repository entry
wget http://packages.erlang-solutions.com/erlang-solutions-1.0-1.noarch.rpm
rpm -Uvh erlang-solutions-1.0-1.noarch.rpm  
## adding the repository entry manually
rpm --import http://packages.erlang-solutions.com/rpm/erlang_solutions.asc
## 如果没报错，则准备环境安装完成

## 安装rabbitMQ 先下载对应安装文件 wget或者scp均可
yum instal rabbitmq-server-3.5.3-1.noarch.rpm

RabbitMQ服务
开启：service rabbitmq-server start
关闭：service rabbitmq-server stop
重启：service rabbitmq-server restart

## RabbitMQ提供了一个web的监控页面系统，这个系统是以Plugin的方式进行调用的，在Documentation下的Server下的Management是关于配置这个插件的。地址:http://www.rabbitmq.com/management.html
## 这个管理插件是包含在RabbitMQ发行包里的，所以只需激活即可。命令如下
abbitmq-plugins enable rabbitmq_management
```
### 监控页面
[具体语法请点击](http://www.rabbitmq.com/man/rabbitmqctl.1.man.html)

[介绍界面](http://www.rabbitmq.com/management.html)


