package test.server;

import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyRequestProcessor;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    public static void main(String[] args) {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        NettyRemotingServer server = new NettyRemotingServer(nettyServerConfig);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        server.registerProcessor(1, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
                System.out.println("服务器端接收到了请求,消息内容: " + new String(request.getBody()));

                RemotingCommand response = RemotingCommand.createResponseCommand(1, "remark");
                response.setBody("好的".getBytes(StandardCharsets.UTF_8));
                return response;
            }

            @Override
            public boolean rejectRequest() {
                return false;
            }
        }, executorService);

        server.start();
    }

}
