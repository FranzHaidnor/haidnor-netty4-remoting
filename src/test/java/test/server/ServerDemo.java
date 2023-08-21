package test.server;

import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyRequestProcessor;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {

    public static void main(String[] args) {
        NettyRemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        server.registerProcessor(Command.GET_SERVER_INFO, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
                return RemotingCommand.createResponse(0,"HelloWorld".getBytes(StandardCharsets.UTF_8));
            }
        }, executorService);


        server.start();
    }

}


