# 数据隧道
>现在工程里面存在很多页面数据传递的需求，但是现有的方法很难理解、很容易误改。其中存在传递好几个页面的参数更是难以维护，最头痛的是开发完成的需求，PM要求加from，代码结构都要破坏。因此基于此，写了个简单的数据隧道功能。

##使用
**@DataTunnelFilter**注解：`key`是当前页面的唯一id，`accepts`是当前页面接收的页面**key的集合**（支持多个页面数据的接收）；
**DataTunnelProtocol**，实现两个方法分别是封装数据和解析数据。

##案例

```java
//初始化
DataTunnel.install(application);
```

```java
//数据源
@DataTunnelFilter(key = "VideoActivity", accepts = {})
public class VideoActivity extends AppCompatActivity implements DataTunnelProtocol {
    @Override
    public Object dataToTunnel() {
        Map<String, Object> map = new HashMap<>();
        map.put("from", "play");
        map.put("videoId", 1);
        return map;
    }
}
```

```java
//数据接收
@DataTunnelFilter(key = "DetailActivity", accepts = {"VideoActivity", "HomeActivity"})
public class DetailActivity extends AppCompatActivity implements DataTunnelProtocol{

    @Override
    public void dataFromTunnel(Map<String, Object> map) {
        map.get("VideoActivity").get("from");
        map.get("VideoActivity").get("videoId");
        ...
    }

}
```


##注意
因为本框架借助Activity和Fragment生命周期实现，所以一般用于界面跳转的数据传递。单个页面的数据不能使用。

