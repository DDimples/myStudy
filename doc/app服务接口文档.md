version: 1.0
date: 2015-08-07
tags: app服务接口初始版
author: 程祥

---

# APP服务接口文档


## 状态码
| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| retcode| 接口返回状态码|-2=系统异常，-1=参数校验错误,0=请求成功|
| retdesc| 接口返回状态消息|对应请求状态的具体消息|

## 登陆服务接口
### 接口功能

> app提供入参：用户名、密码、clientid(非必输)验证登陆用户的合法性，失败返回具体原因密码错误或者账号错误；成功则返回用户名、身份证号码、工作状态、今日完成订单数；

**思考与讨论：** 需要添加deviceid、location坐标吗？用于记录业务员的登陆手机设备号和登陆用户的坐标位置 

### 接口设计

- 请求地址：/app/login
- 请求方式：post
- 返回格式：json

### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
| userpw| String|Y|用户密码| 12345678|
| clientid| String|Y|个推注册ID| test1234568|

### 出参

| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| userid| 用户账号||
| cardid| 身份证号||
| iwstatus| 工作状态|0=不接单；1=待接单；2=已接单，派送中|
| ifinorder| 今日完成订单数||

### 返回示例
```
{
	"retcode":0,
	"retdesc":"success",
	"userid":"",
	"cardid":"",
	"iwstatus":0,
	"ifinorder",10
}
```

 
## 退出服务接口

### 接口功能
> 用户退出

### 接口设计

- 请求地址：/app/logout
- 请求方式：post
- 返回格式：json

### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
**思考与讨论：** 如何校验该请求是用户本人所发,后期如何完善？
- 第一次登陆时发送设备号，登陆成功后每次请求都必须带入设备号进行验证是否是有效请求；
- 第一次登陆时，服务端根据用户id、设备号等生成一个时效性的token，返回给app，app每次请求时带入token进行验证；

### 出参


| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| istatus| 退出状态|0=代表退出成功；1=代表失败|

### 返回示例

```
{
	"retcode":0,
	"retdesc":"success",
	"istatus":0
}
```

## 修改密码服务接口

### 接口功能

> 根据用户的请求，修改密码。请求中包含原密码和新密码；两次密码一致性校验在客户端做；

### 接口设计

- 请求地址：/app/changepw
- 请求方式：post
- 返回格式：json

### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
| oldpw| String|Y| 旧密码|12346789|
| newpw| String|Y| 新密码|12346789|

### 出参

| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| istatus| 修改密码状态 |0=代表修改成功；1=代表失败|

### 返回示例

```
{
	"retcode":0,
	"retdesc":"success",
	"istatus":0
}
```


## 开始接单服务接口

### 接口功能

### 接口设计

- 请求地址：/app/begorders
- 请求方式：post
- 返回格式：json

### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
| latitude| String|Y| 纬度|42.4544545|
| longitude| String|Y| 经度|112.4564645|

**思考与讨论：** 此处选择发送经纬度还是地址名称？
- 经纬度后期可以被利用到自动派单，计算距离；
- 前期业务员手动派单时需要知道地址，通过后台也可以处理显示；



### 出参

| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| istatus| 确认状态|0=代表成功；1=代表失败|


### 返回示例

```
{
	"retcode":0,
	"retdesc":"success",
	"istatus":0
}
```

## 订单列表服务接口

### 接口功能

> 返回不同状态的订单列表
> 向用户发送（**在距离范围内的**）新订单列表，供用户选择；

**思考与讨论：**后期会增加这个功能吗？自动派单
### 接口设计

- 请求地址：/app/neworderlist
- 请求方式：post
- 返回格式：json


### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
| orderstatus|String|Y| 订单状态|0、1、2、3）|

**都有哪些订单状态？**待派单、已接单、派送中、已完成。。。

### 出参

| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| pk_order|订单id|
| busitype|业务类型|
| uaddress|用户地址|
| ordertime|预约时间|
| buyaddress|购买地点|
| iordstatus|订单状态|0、1、2、3有哪些订单状态|
| orderamount|订单金额|
| ipaytype|支付方式|0=在线支付，1=现金支付</br>|

**思考与讨论：** 业务类型、订单状态、支付方式类似这种枚举类型的值返回字符值还是int？

### 返回示例

```
{
	"retcode":0,
	"retdesc":"success",
	"list":[
		{
			"pk_order":"201408064646",
			"busitype":"类型一",
			"uaddress":"北京市昌平区回龙观",
			"ordertime":"2015-08-08 15:20:20",
			"buyaddress":"北京市昌平区霍营",
			"iordstatus":1,
			"orderamount":100,
			"ipaytype":0
		},
		{
			"pk_order":"201408067542",
			"busitype":"类型二",
			"uaddress":"北京市昌平区回龙观",
			"ordertime":"2015-08-08 15:20:20",
			"buyaddress":"北京市昌平区霍营",
			"iordstatus":0,
			"orderamount":105,
			"ipaytype":1
		}
	]
}
```


## 订单详情服务接口

### 接口功能

> 查看新订单详细信息；

**思考与讨论: ** 需求上写要做定时任务？到订单详情列表后再点击接单，此时判断此单是否是最新状态，是否修改过，是否已被接单；每次刷新订单列表时，获取最新的订单列表即可。
  
### 接口设计

- 请求地址：/app/orderdetail
- 请求方式：post
- 返回格式：json

### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
| orderid| String|Y| 订单号|12346789|

### 出参

| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| pk_order|订单id|
| cusname| 联系人名|
| custell| 联系人电话|
| cusaddress| 客户地址|
| ordertime|下单时间|
| ipaytype|支付方式|0-在线支付，1-现金支付|
| buyaddress|购买地点|
| goodsname|商品名称|
| goodsnum|商品数量|
| goodsimgurl|商品图片链接|
| goodsnotes|商品备注|
| ordernotes|订单备注|
| iordstatus|订单状态| 有哪些状态？|


### 返回示例

```
{
	"retcode":0,
	"retdesc":"success",
	"pk_order":"201508078965",
	"cusname":"香辣鸡腿堡",
	"custell":"13591996983",
	"cusaddress":"北京市昌平区",
	"ordertime":"2015-08-07 10:01:08",
	"ipaytype":0,
	"buyaddress":"北京市昌平区霍营",
	"goodsname":"香辣鸡腿堡",
	"goodsnum":3,
	"goodsimgurl":"http://test.com/aaa.jpg",
	"goodsnotes":"套餐",
	"ordernotes":"多鸡腿，少放辣",
	"iordstatus":1
}
```

## 已完成订单列表服务接口

### 接口功能

 > 与新增订单列表合并,加一个订单状态的入参即可



## 订单统计服务接口

### 接口功能

> 统计指定日期天的订单统计情况，展现当天金额合计及明细

### 接口设计

- 请求地址：/app/orderstatistic
- 请求方式：post
- 返回格式：json


### 入参

| 参数	|类型	|是否必输	|说明	|实例	|
| :-----:|:------:|:-----:|:------:|:-----:|
| userid| String|Y| 用户账号|12346789|
| checkid|String|Y| 校验号|4654654654fdsaf|
| countdate| String|Y| 统计日期|2015-08-09|

### 出参

| 参数	|说明	|对应值	|
| :-----:|:------:|:------:|
| ordercountmny|订单总金额|
| onlinepaycountmny|在线支付总金额|
| cashpaycountmny|现金支付总金额|
| onlservicemny|在线支付服务费总金额|
| cashservicemny|现金支付服务费总金额|
| pk_order|订单id|
| busitype|业务类型|
| uaddress|用户地址|
| orderamount|订单金额|
| ipaytype|支付方式|0-在线支付，1-现金支付|

### 返回示例

```
{
	"retcode":0,
	"retdesc":"success",
	"ordercountmny":6253.123,
	"onlinepaycountmny":5000.123,
	"cashpaycountmny":1253.000,
	"onlservicemny":600.000,
	"cashservicemny":100.000,
	"list":[
		{
			"pk_order":"201408064646",
			"busitype":"类型一",
			"uaddress":"北京市昌平区回龙观",
			"orderamount":100,
			"ipaytype":0
		},
		{
			"pk_order":"201408067542",
			"busitype":"类型二",
			"uaddress":"北京市昌平区回龙观",
			"orderamount":105,
			"ipaytype":1
		}
	]
}
```









