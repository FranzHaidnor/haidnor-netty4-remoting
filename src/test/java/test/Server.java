package test;

import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyRequestProcessor;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server {

    public static void main(String[] args) {
        NettyServerConfig config = new NettyServerConfig();
        config.setFrameMaxLength(Integer.MAX_VALUE);
        config.setServerSocketSndBufSize(Integer.MAX_VALUE);
        config.setServerSocketRcvBufSize(Integer.MAX_VALUE);
        NettyRemotingServer server = new NettyRemotingServer(config, Command.class);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        server.registerProcessor(Command.GET_SERVER_INFO, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
                log.info("服务端接收到了消息-------------------------{}", request.getOpaque());
                return RemotingCommand.createResponse(0, "ok!", "XXXXXXXXXX".getBytes(StandardCharsets.UTF_8));
            }
        }, executorService);

        server.start();
    }


}


