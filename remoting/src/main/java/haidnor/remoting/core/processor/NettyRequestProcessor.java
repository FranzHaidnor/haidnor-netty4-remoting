package haidnor.remoting.core.processor;

import haidnor.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * Common remoting command processor
 */
public interface NettyRequestProcessor {

    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception;

    default boolean rejectRequest() {
        return false;
    }

}
