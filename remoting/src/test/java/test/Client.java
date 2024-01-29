package test;

import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.protocol.RemotingCommand;
import lombok.SneakyThrows;
import test.model.User;

import java.nio.charset.StandardCharsets;

public class Client {

    @SneakyThrows
    public static void main(String[] args) {
        NettyClientConfig config = new NettyClientConfig();
        config.setFrameMaxLength(Integer.MAX_VALUE);
        config.setClientSocketSndBufSize(Integer.MAX_VALUE);
        config.setClientSocketRcvBufSize(Integer.MAX_VALUE);
        NettyRemotingClient client = new NettyRemotingClient(config);

        // 同步发送
        {
            User user = new User(1L,"25684598_&WAfadfa");
            RemotingCommand request = RemotingCommand.creatJsonProtocolRequest("/user/login", user);
            RemotingCommand response = client.invokeSync("127.0.0.1:8080", request, 10 * 1000);
            System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
        }

//        // 同步发送
//        {
//            RemotingCommand request = RemotingCommand.creatRequest(CommandEnum.GET_SERVER_INFO, null, null);
//            RemotingCommand response = client.invokeSync("127.0.0.1:8080", request, 10 * 1000);
//            System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
//        }
//
//        // 异步发送
//        {
//            RemotingCommand request = RemotingCommand.creatRequest(CommandEnum.GET_SERVER_INFO, null, null);
//            client.invokeAsync("127.0.0.1:8080", request, responseFuture -> {
//                RemotingCommand response = responseFuture.getResponseCommand();
//                System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
//            });
//        }
//
//        // 单向发送
//        {
//            RemotingCommand request = RemotingCommand.creatRequest(CommandEnum.GET_SERVER_INFO, null, null);
//            client.invokeOneway("127.0.0.1:8080", request);
//        }
    }


}

