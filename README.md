# haidnor-netty4-remoting
基于 RocketMQ Remoting 模块源码二次开发，用于快速构建 Netty Server / Client。

# maven pom 文件配置
```xml
<dependency>
    <groupId>haidnor</groupId>
    <artifactId>haidnor-netty4-remoting</artifactId>
    <version>1.0</version>
</dependency>
```

# 快速使用
## 约定客户端-服务端请求命令
要求有一个自定义枚举，枚举名称没有要求，无需有任何属性字段。haidnor-netty4-remoting 将会使用使用枚举中的类型，作为服务端与客户端通信的接口路径。

原理：客户端和服务端会解析枚举类型名称的 hashCode 值，将其作为请求指令互相传递，因此有可能会出现 hashCode 冲突。
所以在构建服务端与客户端时需要将自定义的指令枚举作为构造参数传入，以检验是否存在枚举类型名称有 hashCode 冲突，若发生 hashCode 冲突，服务端或客户端将会无法启动，并且提示哪些枚举类型的名称需要修改。
请注意枚举名称发生 hashCode 冲突是一个极小概率的事件，并不会影响到编码体验。
```java
public enum Command {
    // 获取服务端信息
    GET_SERVER_INFO,
    
    // 获取客户端信息
    GET_CLIENT_INFO;
}
```
## 构建服务端
```java
public class ServerDemo {

    public static void main(String[] args) {
        // 参数1:服务端配置参数 参数2:指令枚举
        RemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);

        // 处理请求的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 注册指令处理器
        server.registerProcessor(Command.GET_SERVER_INFO, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
                System.out.println("服务器端接收到了请求 GET_SERVER_INFO, 消息内容: " + new String(request.getBody()));
                return RemotingCommand.createResponse("OK".getBytes(StandardCharsets.UTF_8));
            }
        }, executorService);

        // 服务器启动
        server.start();
    }
    
}
```
`NettyServerConfig` 配置类中默认的服务端口为 8080

## 构建客户端
```java
public class ClientDemo {

    public static void main(String[] args) {
        // 参数1:客户端配置类 参数2:服务端地址 参数3:指令枚举
        RemotingClient client = new NettyRemotingClient(new NettyClientConfig(), "127.0.0.1:8080", Command.class);

        // 构建请求消息体
        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO);

        try {
            // 同步发送请求
            RemotingCommand response = client.invokeSync(request);

        } catch (InterruptedException e) {
            // do something
        } catch (RemotingConnectException e) {
            // do something
        } catch (RemotingSendRequestException e) {
            // do something
        } catch (RemotingTimeoutException e) {
            // do something
        }

    }

}
```