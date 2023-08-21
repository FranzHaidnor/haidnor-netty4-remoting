package test.server;

import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.protocol.RemotingCommand;
import lombok.SneakyThrows;

public class ClientDemo {

    @SneakyThrows
    public static void main(String[] args) {
        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig());

        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO);

        // 同步发送
        RemotingCommand response = client.invokeSync("127.0.0.1:8080", request);

        // 异步发送
        client.invokeAsync("127.0.0.1:8080", request, responseFuture -> {
            byte[] body = response.getBody();
            System.out.println(new String(response.getBody()));
        });

        // 发送后不管
        client.invokeOneway("127.0.0.1:8080", request);
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
