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
