
title: Web项目中使用Maven	
date: 2015-11-30 13:18:02
tags: web maven

---

## 背景

> 在程序开发过程中，免不了引用jar包和项目打包。以往引用jar包的操作是手动将jar包拷到lib目录下，如果需要更换版本就要将原来的删除，然后拷贝新版本的jar包。使用maven可以通过配置文件来管理jar包的引用，新增、删除和修改只需要更改配置文件即可。 maven也支持项目打包：.jar、.war、tar.gz等等很多复杂的包都支持，而且配置灵活。

<!--more-->

## Maven简介及使用


[Maven简介](http://maven.apache.org/index.html)： 简单来说，maven是大名鼎鼎的Apache下的一个开源工具，可以灵活构造项目，可以动态管理项目依赖jar包等。

### 安装

[官网上的介绍](http://maven.apache.org/install.html)

* 依赖java
* 解压文件到安装目录下 unzip apache-maven-3.3.9-bin.zip
* 添加maven的bin文件夹路径到环境变量中：在.bash_profile中添加PATH=$MAVEN_HOME/bin:$PATH，source ~/.bash_profile 生效
* 查看安装成功：mvn -v 出现类似如下输出，则安装成功。

```
Apache Maven 3.2.3 (33f8c3e1027c3ddde99d3cdebad2656a31e8fdf4; 2014-08-12T04:58:10+08:00)
Maven home: /usr/local/apache-maven-3.2.3
Java version: 1.7.0_71, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.7.0_71.jdk/Contents/Home/jre
Default locale: zh_CN, platform encoding: UTF-8
OS name: "mac os x", version: "10.10.5", arch: "x86_64", family: "mac"
```

### 配置

* 需要在当前用户目录下新建一个 .m2文件夹
* 在.m2目录下[配置setting.xml](http://maven.apache.org/settings.html) （非必要，有自己的私服则可以配置）
* 上面的setting.xml可以不添加，因为MAVEN_HOME/conf/下有一个默认的setting.xml，需要在.m2下新增一个repository文件夹，用来存放下载的jar包

### 使用

* web项目中每个模块都有自己的pom.xml文件，在pom文件中配置当前model所依赖的jar包引用,此时maven会自动将jar包下载，并引用。如：

``` 

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
         
    <artifactId>study-parent</artifactId>
    <groupId>com.mystudy.web</groupId>
    <version>1.0.0-SNAPSHOT</version>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>common</artifactId>

    <properties>
        <log4j2-version>2.4.1</log4j2-version>
        <org.springframework-version>4.2.3.RELEASE</org.springframework-version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

</project>

```

* 打包更方便，只需要在pom.xml文件配置后运行响应的命令即可，[详细使用方法请点击](http://maven.apache.org/run.html)。下面是个栗子：

1. 项目分为多个模块：model、common、dao、service、web五个模块，其中dao/service/web都依赖model和common模块，service依赖dao，web依赖service项目。当我打包的时候model、common、dao、service都应该是jar包，web模块是war包。
2. maven默认打jar包，所以只需要配置web模块的pom文件即可：添加<build> bean，[各种详细配置方式请点击](http://maven.apache.org/guides/mini/guide-configuring-plugins.html)
3. [web模块下pom文件链接](https://github.com/DDimples/myStudy/blob/master/webDemo/web/pom.xml)

```
<build>
    <!-- 此处是该模块打包出来的最终名字，不配置则是模块名+版本号+后缀-->
    <finalName>myStudy</finalName>
    <defaultGoal>install</defaultGoal>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <skip>true</skip>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>2.5.1</version>
            <configuration>
                <source>1.6</source>
                <target>1.6</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        <plugin>
            <!-- 打war包的插件-->
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>2.3</version>
            <configuration>
                <webXml>src/main/webapp/WEB-INF/web.xml</webXml>
                <!-- 这里可以指定当前模块下的文件夹、文件，maven会把它当成资源文件copy到targetPath目录下 -->
                <!--  
                <webResources>
                    <resource>
                        <directory>webapp/resource/</directory>
                        <filtering>true</filtering>
                        <targetPath>WEB-INF</targetPath>
                    </resource>
                </webResources>
                -->
            </configuration>
        </plugin>
    </plugins>
</build>
```

* pom文件还有很多灵活的配置，比如filter、resources等等[详情请点击](http://maven.apache.org/plugins/maven-resources-plugin/examples/filter.html)
* 父模块的pom文件可以决定打包的顺序，如：，

```
<modelVersion>4.0.0</modelVersion>
<groupId>com.mystudy.web</groupId>
<artifactId>study-parent</artifactId>
<packaging>pom</packaging>
<version>1.0.0-SNAPSHOT</version>
<modules>
    <module>model</module>
    <module>common</module>
    <module>dao</module>
    <module>service</module>
    <module>web</module>
</modules>
```

* 执行打包命令：mvn clean install,最终会有如下输出：

```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] study-parent ....................................... SUCCESS [  0.420 s]
[INFO] model .............................................. SUCCESS [  1.939 s]
[INFO] common ............................................. SUCCESS [  1.664 s]
[INFO] dao ................................................ SUCCESS [  0.089 s]
[INFO] service ............................................ SUCCESS [  0.199 s]
[INFO] web ................................................ SUCCESS [  2.693 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 7.169 s
[INFO] Finished at: 2015-12-03T18:04:46+08:00
[INFO] Final Memory: 27M/222M
[INFO] ------------------------------------------------------------------------


```

* 在web模块web/target/目录下会有myStudy.war，这个文件中包含所项目所依赖的所有jar包及编译过后的class文件，直接拷贝到tomcat  webapp目录下启动tomcat即可访问。


## 结语

maven工具极大的方便了开发及打包的方式，而且基于配置后可以很灵活的使用各种带参数的命令，官方文档也很详细。


> ***欢迎拍砖，批评是进步的动力！ 谢谢~***





