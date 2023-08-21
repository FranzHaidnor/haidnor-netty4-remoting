package test.server;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;
import lombok.SneakyThrows;

public class ClientDemo {

    @SneakyThrows
    public static void main(String[] args) {
        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig(), "127.0.0.1:8080");

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
            public void onChannelAllIdle(String remoteAddr, Channel channel) {
                System.out.println("onChannelIdle");
            }
        };
        client.registerChannelEventListener(eventListener);


        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO);
        RemotingCommand response = client.invokeSync(request);



        client.invokeOneway(request);
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
