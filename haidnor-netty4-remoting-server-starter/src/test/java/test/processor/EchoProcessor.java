package test.processor;

import haidnor.remoting.client.spring.common.annotation.NettyRemotingRequestProcessor;
import haidnor.remoting.core.processor.NettyRequestProcessor;
import haidnor.remoting.protocol.RemotingCommand;
import haidnor.remoting.protocol.RemotingSysResponseCode;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@NettyRemotingRequestProcessor("/test/echo")
@Component
public class EchoProcessor implements NettyRequestProcessor {

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        String msg = new String(request.getBody());
        System.out.println(msg);
        return RemotingCommand.createResponse(RemotingSysResponseCode.SUCCESS, msg.getBytes("UTF-8"));
    }

}
