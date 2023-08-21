package test.server;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.RPCHook;
import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDemo {

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
            public void onChannelAllIdle(String remoteAddr, Channel channel) {
                System.out.println("onChannelIdle");
            }
        };
        server.registerChannelEventListener(eventListener);


        ExecutorService executorService = Executors.newFixedThreadPool(4);
//        server.registerProcessor(Command.GET_SERVER_INFO, (ctx, request) -> RemotingCommand.createResponse("OK".getBytes(StandardCharsets.UTF_8)), executorService);

        RPCHook hook1 = new RPCHook() {
            @Override
            public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
                // do something
            }

            @Override
            public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
                // do something
            }
        };
        server.registerRPCHook(hook1);

        RPCHook hook2 = new RPCHook() {
            @Override
            public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
                // do something
            }

            @Override
            public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
                // do something
            }
        };
        server.registerRPCHook(hook2);

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


