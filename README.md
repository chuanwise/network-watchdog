# Network Watchdog - 服务器网络状态看门狗

本插件意在当 `Minecraft` 服务器宿主网络故障时，自动关闭 `Minecraft` 服务器。 以避免故障时，可能对宿主机进行的硬重启等操作杀死服务器而导致插件未能正确关闭，引发数据丢失等问题。

## 功能

本插件具备的功能如下：

* 通过建立与指定 URL 的连接来检查网络是否畅通，并记录检测结果。
* 网络连续几次不畅通时，触发事件后关闭服务器。
* 在检测网络畅通的过程中，可以每次可发送一些消息以避免被误认为 DDoS。

## 配置

### 配置 - `configuration.yml`

```yaml
# 插件功能是否启动
enable: true

# 是否显示调试日志
debug: false

# 每次随机访问哪些 URL 以验证网络是否畅通
# 如果你有服务器（如静态资源服务器），则建议 URL 指向你自己的服务器，以免被误认为是 DDoS。
# 否则可以写一些比较稳定的 URL，如百度和 QQ，开启自述功能（introduce: true），
# 同时写多一些以降低请求相同 URL 的频率。
urls:
  - https://www.qq.com
  - https://www.baidu.com
  - https://cn.bing.com

# 是否在发送 HTTP 请求时带上自述信息（避免被认为是 DDoS）。
# 若开启，每次访问任一 URL 时，若发现是 Http URL，则向其发送该消息（位于 Http 头的 User-Agent）
# 自述信息的内容在 messages.yml 的 self-introduction。
# 若需开启，请修改 self-introduction 后再将此项设置为 true。
introduce: false

# 经过多少个时间刻时检查一次网络
# 插件不会在主线程上网络 IO，不必担心因此卡服。
# 一刻是 1/20 秒，因此默认值 6000 即 5 分钟检查一次。
# 这个频率不应设置的过高，避免被认为是 DDoS
interval: 6000

# 连续多少次检查失败时关服，默认 1 次，即发现网络异常时立刻关服。
# 如果你访问的是一些不太稳定的 URL，则可以把此项设置的高一点。
threshold: 1
```

### 消息 - `messages.yml`

```yaml
# 自我介绍消息。
self-introduction: |
  Hello, this is (name).
  I need to test whether the network is connected, so I sent you this message. 
  On average, you will receive this message every ${1} second(s).
  My email is: (email). If you have any questions, please contact me.
```

默认设置是建议的内容，含义如下：

```
你好，这里是（你的昵称）。
我需要检测网络何时断开，所以向你发送了这条消息。
平均每 ${1} 后你会收到一次这条消息。
我的邮件是：（你的邮件）。如果你有任何疑问，请联系我。
```

## 权限

### network-watchdog.reload

使用 `/nw reload` 重载插件配置的权限。

### network-watchdog.debug

使用 `/nw reload` 出现异常时显示异常信息的权限。