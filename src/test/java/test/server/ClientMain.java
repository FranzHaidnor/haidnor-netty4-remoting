package test.server;

import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.exception.RemotingConnectException;
import haidnor.remoting.exception.RemotingSendRequestException;
import haidnor.remoting.exception.RemotingTimeoutException;
import haidnor.remoting.exception.RemotingTooMuchRequestException;
import haidnor.remoting.protocol.RemotingCommand;

import java.nio.charset.StandardCharsets;

public class ClientMain {

    public static void main(String[] args) throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, RemotingTooMuchRequestException {

        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig("127.0.0.1:8080"), Command.class);

        RemotingCommand request = RemotingCommand.creatRequest(Command.SERVER_HELLO.name(), "Hello,World".getBytes(StandardCharsets.UTF_8));
        // 含有自定义请求头的消息
        RemotingCommand request2 = RemotingCommand.creatRequest(Command.SERVER_HELLO.name(), new CommandHeader("31", 18), "Hello,World".getBytes(StandardCharsets.UTF_8));

        // 同步发送
        {
            // 默认超时时间 5000ms
            RemotingCommand response = client.invokeSync(request);
            System.out.println(response);

//            RemotingCommand response2 = client.invokeSync(request, 1000);
//            System.out.println(response2);
        }
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

    }

}
