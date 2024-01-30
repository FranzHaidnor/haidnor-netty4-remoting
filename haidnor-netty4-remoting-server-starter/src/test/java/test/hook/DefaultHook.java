package test.hook;

import haidnor.remoting.RPCHook;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingRPCHook;
import haidnor.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@NettyRemotingRPCHook
@Component
public class DefaultHook implements RPCHook {

    private static final Logger log = LoggerFactory.getLogger(DefaultHook.class);


    @Override
    public void doBeforeRequest(String remoteAddr, RemotingCommand request) {
        log.info("doBeforeRequest --------------------------------");
    }

    @Override
    public void doAfterResponse(String remoteAddr, RemotingCommand request, RemotingCommand response) {
        log.info("doAfterResponse --------------------------------");
    }

}
