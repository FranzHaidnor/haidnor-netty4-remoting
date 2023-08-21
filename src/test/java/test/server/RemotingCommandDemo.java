package test.server;

import haidnor.remoting.RemotingClient;
import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.protocol.RemotingCommand;
import lombok.SneakyThrows;

public class RemotingCommandDemo {

    @SneakyThrows
    public static void main(String[] args) {
        RemotingClient client = new NettyRemotingClient(new NettyClientConfig());

        RemotingCommand request = RemotingCommand.creatRequest(Command.GET_SERVER_INFO);

        // 同步请求
        RemotingCommand response1 = client.invokeSync("127.0.0.1:8080", request);
        RemotingCommand response2 = client.invokeSync("127.0.0.1:8080", request, 1000); // 设置超时时间 1000 毫秒 (默认值 5000 毫秒)

        // 异步请求
        client.invokeAsync("127.0.0.1:8080", request, responseFuture -> {
            RemotingCommand response = responseFuture.getResponseCommand();
        });
        client.invokeAsync("127.0.0.1:8080", request, 1000, responseFuture -> {  // 设置超时时间 1000 毫秒 (默认值 5000 毫秒)
            RemotingCommand response = responseFuture.getResponseCommand();
        });

        // 单向请求 （无返回结果）
        client.invokeOneway("127.0.0.1:8080", request);
        client.invokeOneway("127.0.0.1:8080", request, 1000); // 设置超时时间 1000 毫秒 (默认值 5000 毫秒)
    }

}
