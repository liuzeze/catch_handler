# 异常捕获, 增加错误日志上传钉钉功能
##  使用方式:

 ```
 //添加gradle 引用  
 implementation 'com.lz:catch_handler:1.0.0'
 
 //在application里初始化 代码 ,钉钉机器人添加方式 可查看 钉钉开发文档
  if (BuildConfig.DEBUG) {
            new UCEHandler.Builder(getApplicationContext())
                    .setServiceUrl("钉钉机器人网址")
                    .build();

        }

 
 ```
 
 # okhttp 接口日志拦截
 ## 使用方式 
 
 ```
 //添加gradle 引用  
 implementation 'com.lz:NetLog:1.0.0'

//自己找个入口调用这句话l
  NetworkLogListActivity.start(v.getContext());

 
 ```
 
 ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-22-58.png)  
  ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-23-15.png)
   ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-23-28.png)
    ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-24-11.png)
     ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-27-05.png)
      ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-27-09.png)
       ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-27-22.png)
       ![image](https://github.com/liuzeze/ScrollTable/blob/master/img/Screenshot_2019-01-14-11-27-25.png)

