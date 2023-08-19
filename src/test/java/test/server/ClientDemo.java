package test.server;

import haidnor.remoting.RemotingClient;
import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.exception.RemotingConnectException;
import haidnor.remoting.exception.RemotingSendRequestException;
import haidnor.remoting.exception.RemotingTimeoutException;
import haidnor.remoting.protocol.RemotingCommand;

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

//    public static void main(String[] args) throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException {
//
//        RemotingClient client = new NettyRemotingClient(new NettyClientConfig(), "127.0.0.1:8080", Command.class);
//
//        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO, "Hello,World".getBytes(StandardCharsets.UTF_8));
//        // 含有自定义请求头的消息
////        RemotingCommand request = RemotingCommand.creatRequest(Command.SERVER_HELLO, new CommandHeader("31", 18), "Hello,World".getBytes(StandardCharsets.UTF_8));
//
//        RemotingCommand request2 = RemotingCommand.creatRequest(Command.GET_CLIENT_INFO);
//
//        // 同步发送
//        {
//            // 默认超时时间 5000ms
//            RemotingCommand response = client.invokeSync(request);
//            System.out.println(response);
//
//            RemotingCommand response2 = client.invokeSync(request2, 1000);
//            System.out.println(response2);
//        }
//
//        // 异步发送
//        {
//            // 默认超时时间 5000ms
//            client.invokeAsync(request, responseFuture -> {
//                RemotingCommand response = responseFuture.getResponseCommand();
//                System.out.println("服务端响应:" + new String(response.getBody()));
//            });
//
//            client.invokeAsync(request, 500, responseFuture -> {
//                RemotingCommand response = responseFuture.getResponseCommand();
//                System.out.println("服务端响应:" + new String(response.getBody()));
//            });
//        }
//
//        // 发送后不管
//        {
//            client.invokeOneway(request);
//            client.invokeOneway(request, 5000);
//        }
//    }
