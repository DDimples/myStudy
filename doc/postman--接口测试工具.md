# 接口测试工具——Postman

## Postman功能

>postman是Chrome浏览器的一个插件，在Chromes设置-扩展程序-加载已解压的扩展程序。选择解压后的文件夹即可； [下载地址](http://pdktest.lifecreatech.com:9000/lifecreate/workdoc/blob/master/utils%20%E5%B7%A5%E5%85%B7/Postman-REST-Client_v0.8.1.rar)

>由于浏览器只可以发送get方式的http请求，但是大部分服务接口都是post方式的HTTP请求；Postman可以模拟浏览器发送各种方式的HTTP请求，便于测试和开发验证服务接口的正确性及性能；


## Postman使用


![link](http://i1.tietuku.com/0241861163b210bd.jpg)

1. 在URL栏中输入API服务地址
2. 选择HTTP请求方法，GET/POST等，点击Headers 添加请求头参数
3. 根据API参数，输入对应值
4. raw中根据接口要求输入对应的请求体串（一般有参数就不需要添加raw）
5. 当参数都填写完毕后，点击send发送请求；点击preview 可以查看请求的header和请求url，点击 add to collection 可以将此操作保存到本地，便于下次使用，还可以分类；
6. 发送请求后，在这一行会返回对应信息，HTTP 状态码、响应时间等；body 里就是返回的值，可以选择JSON、XML等格式查看；

