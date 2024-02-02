package test.hook;

import haidnor.remoting.RPCHook;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingRPCHook;
import haidnor.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@NettyRemotingRPCHook
@Component
@Order(3)
public class DefaultHook2 implements RPCHook {

    private static final Logger log = LoggerFactory.getLogger(DefaultHook2.class);

    @Override
    public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
        log.info("doBeforeRequest");
    }

    @Override
    public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
        log.info("doAfterResponse");
    }

}
