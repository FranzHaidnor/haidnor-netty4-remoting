package test.server;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyRequestProcessor;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.protocol.RemotingCommand;
import haidnor.remoting.util.RemotingHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    public static void main(String[] args) {
        NettyRemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);

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
            public void onChannelIdle(String remoteAddr, Channel channel) {
                System.out.println("onChannelIdle");
            }
        };
        server.registerChannelEventListener(eventListener);


        ExecutorService executorService = Executors.newFixedThreadPool(5);
        server.registerProcessor(Command.SERVER_HELLO, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
                String clientAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                System.out.println("服务器端接收到了请求,消息内容: " + new String(request.getBody()));
                return RemotingCommand.createResponse("好的".getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public boolean rejectRequest() {
                return false;
            }
        }, executorService);

        server.start();

    }

}
