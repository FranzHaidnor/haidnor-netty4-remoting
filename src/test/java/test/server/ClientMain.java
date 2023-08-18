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

        NettyRemotingClient client = new NettyRemotingClient(new NettyClientConfig("127.0.0.1:8080"));
        client.start();

//        RemotingCommand request = RemotingCommand.createRequestCommand(1, new CommandHeader("张三", 18), "Hello,World".getBytes(StandardCharsets.UTF_8));

        RemotingCommand request = RemotingCommand.createRequestCommand(1, "Hello,World".getBytes(StandardCharsets.UTF_8));

        // 同步发送
        RemotingCommand command = client.invokeSync(request, 1000);
        System.out.println(command);

        // 异步发送
        client.invokeAsync(request, 500, responseFuture -> {
            RemotingCommand response = responseFuture.getResponseCommand();
            System.out.println("服务端响应:" + new String(command.getBody()));
        });

        // 发送后不管
        client.invokeOneway(request, 5000);
    }

}
