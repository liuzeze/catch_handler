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
 

<table align="center">
    <tr align="center">
      <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-22-58.png" width="280" height="280"/></td>
        <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-23-15.png" width="280" height="280"/></td>
         <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-24-11.png" width="280" height="280"/></td>
         <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-27-05.png" width="280" height="280"/></td>
    </tr>
        <tr align="center">
         <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-27-09.png" width="280" height="280"/></td>
         <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-27-22.png" width="280" height="280"/></td>
         <td><img src="https://github.com/liuzeze/catch_handler/blob/master/img/Screenshot_2019-01-14-11-27-25.png" width="280" height="280"/></td>
    </tr>
</table>

