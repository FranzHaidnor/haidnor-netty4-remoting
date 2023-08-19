# haidnor-netty4-remoting
基于 RocketMQ(4.9.1) Remoting 模块源码二次开发，用于快速构建 Netty Server / Client。

# 配置
maven pom.xml
```xml
<dependency>
    <groupId>haidnor</groupId>
    <artifactId>haidnor-netty4-remoting</artifactId>
    <version>1.0</version>
</dependency>
```

# 快速使用
## 约定请求命令
客户端与服务端和要求有一个公用的自定义枚举，定义彼此通信的端点。（枚举名称没有要求，不需要任何属性字段）。

原理：客户端和服务端会解析枚举类型名称的 hashCode 值，将其作为请求指令互相传递，因此有可能会出现 hashCode 冲突。
所以在构建服务端与客户端时需要将自定义的指令枚举作为构造参数传入，以检验是否存在枚举类型名称有 hashCode 冲突，若发生 hashCode 冲突，服务端或客户端将会无法启动，并且提示哪些枚举类型的名称需要修改。
请注意枚举名称发生 hashCode 冲突是一个极小概率的事件，并不会影响到编码体验。
```java
public enum Command {
    // 获取服务端信息
    GET_SERVER_INFO,
    
    // 获取客户端信息
    GET_CLIENT_INFO
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
# 服务端 API 与特性
## 注册通道事件监听器
```java
public class ServerDemo {

    public static void main(String[] args) {
        NettyRemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);

        // 通道事件监听器
        ChannelEventListener eventListener = new ChannelEventListener() {
            @Override
            public void onChannelConnect(String remoteAddr, Channel channel) {
                System.out.println("onChannelConnect");
            }

            @Override
            public void onChannelClose(String remoteAddr, Channel channel) {
                System.out.println("onChannelClose");
            }

            @Override
            public void onChannelException(String remoteAddr, Channel channel) {
                System.out.println("onChannelException");
            }

            @Override
            public void onChannelIdle(String remoteAddr, Channel channel) {
                System.out.println("onChannelIdle");
            }
        };
        // 注册通道事件监听器
        server.registerChannelEventListener(eventListener);

        server.start();
    }

}
```
## 注册事件处理器钩子方法
```java
public class ServerDemo {

    public static void main(String[] args) {
        NettyRemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        server.registerProcessor(Command.GET_SERVER_INFO, (ctx, request) -> RemotingCommand.createResponse("OK".getBytes(StandardCharsets.UTF_8)), executorService);

        RPCHook hook1 = new RPCHook() {
            @Override
            public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
                // do something
            }

            @Override
            public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
                // do something
            }
        };
        server.registerRPCHook(hook1);

        RPCHook hook2 = new RPCHook() {
            @Override
            public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
                // do something
            }

            @Override
            public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
                // do something
            }
        };
        server.registerRPCHook(hook2);

        server.start();
    }
    
}
```
服务端支持为 NettyRequestProcessor 添加多个前后置处理器。 以上代码在处理 `Command.GET_SERVER_INFO` 指令时会先依次执行 hook1、hook2 的 `doBeforeRequest()` 方法，NettyRequestProcessor 执行完以后再依次执行 hook1、hook2 的 `doAfterResponse()` 方法。

# 客户端 API 与特性
## 同步请求、异步请求、单向请求
```java
public class ClientDemo {

    @SneakyThrows
    public static void main(String[] args) {
        RemotingClient client = new NettyRemotingClient(new NettyClientConfig(), "127.0.0.1:8080", Command.class);

        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO);

        // 同步请求
        RemotingCommand response1 = client.invokeSync(request);

        // 异步请求
        client.invokeAsync(request, responseFuture -> {
            RemotingCommand response = responseFuture.getResponseCommand();
        });

        // 单向请求 （无返回结果）
        client.invokeOneway(request);
    }

}
```

## 注册请求钩子方法
```java
public class ClientDemo {

    @SneakyThrows
    public static void main(String[] args) {
        RemotingClient client = new NettyRemotingClient(new NettyClientConfig(), "127.0.0.1:8080", Command.class);

        RPCHook hook1 = new RPCHook() {
            @Override
            public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
                // do something
            }

            @Override
            public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
                // do something
            }
        };
        client.registerRPCHook(hook1);

        RPCHook hook2 = new RPCHook() {
            @Override
            public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
                // do something
            }

            @Override
            public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
                // do something
            }
        };
        client.registerRPCHook(hook2);

        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO);
        RemotingCommand response = client.invokeSync(request);
    }

}
```
客户端支持为请求方法添加多个前后置处理器。 以上代码执行 `client.invokeSync(request)` 请求时会先依次执行 hook1、hook2 的 `doBeforeRequest()` 方法，请求执行完以后再依次执行 hook1、hook2 的 `doAfterResponse()` 方法。若客户端发送但向请求 `invokeOneway()` 则不会回调执行 `doAfterResponse()` 方法

## 注册通道事件监听器
```java
public class ClientDemo {

    @SneakyThrows
    public static void main(String[] args) {
        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig(), "127.0.0.1:8080", Command.class);

        ChannelEventListener eventListener = new ChannelEventListener() {
            @Override
            public void onChannelConnect(String remoteAddr, Channel channel) {
                System.out.println("onChannelConnect");
            }

            @Override
            public void onChannelClose(String remoteAddr, Channel channel) {
                System.out.println("onChannelClose");
            }

            @Override
            public void onChannelException(String remoteAddr, Channel channel) {
                System.out.println("onChannelException");
            }

            @Override
            public void onChannelIdle(String remoteAddr, Channel channel) {
                System.out.println("onChannelIdle");
            }
        };
        client.registerChannelEventListener(eventListener);
    }

}
```
