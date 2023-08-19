package test.server;

import haidnor.remoting.RemotingServer;
import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyRequestProcessor;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.protocol.RemotingCommand;
import haidnor.remoting.util.RemotingHelper;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {

    public static void main(String[] args) {
        // 参数1:服务端配置参数 参数2:指令枚举
        RemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);

        // 处理请求的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 注册指令处理器
        server.registerProcessor(Command.GET_SERVER_INFO, new NettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
                String clientAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                System.out.println("服务器端接收到了请求 SERVER_HELLO,消息内容: " + new String(request.getBody()));
                return RemotingCommand.createResponse("好的".getBytes(StandardCharsets.UTF_8));
            }
        }, executorService);

        // 服务器启动
        server.start();
    }

}
//    public static void main(String[] args) {
//        NettyRemotingServer server = new NettyRemotingServer(new NettyServerConfig(), Command.class);
//
//        ChannelEventListener eventListener = new ChannelEventListener() {
//            @Override
//            public void onChannelConnect(String remoteAddr, Channel channel) {
//                System.out.println("onChannelConnect");
//            }
//
//            @Override
//            public void onChannelClose(String remoteAddr, Channel channel) {
//                System.out.println("onChannelClose");
//            }
//
//            @Override
//            public void onChannelException(String remoteAddr, Channel channel) {
//                System.out.println("onChannelException");
//            }
//
//            @Override
//            public void onChannelIdle(String remoteAddr, Channel channel) {
//                System.out.println("onChannelIdle");
//            }
//        };
//        server.registerChannelEventListener(eventListener);
//
//
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        server.registerProcessor(Command.GET_SERVER_INFO, (ctx, request) -> {
//            String clientAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
//            System.out.println("服务器端接收到了请求 SERVER_HELLO,消息内容: " + new String(request.getBody()));
//            return RemotingCommand.createResponse("好的".getBytes(StandardCharsets.UTF_8));
//        }, executorService);
//
//
//        server.registerProcessor(Command.GET_CLIENT_INFO, (ctx, request) -> {
//            String clientAddr = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
//            System.out.println("服务器端接收到了请求 SERVER_GET_BROKER_INFO");
//            return RemotingCommand.createResponse("BROKER_233333".getBytes(StandardCharsets.UTF_8));
//        }, executorService);
//
//        server.start();
//
//    }


