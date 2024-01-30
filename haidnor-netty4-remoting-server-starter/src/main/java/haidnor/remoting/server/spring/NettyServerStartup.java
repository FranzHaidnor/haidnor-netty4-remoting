package haidnor.remoting.server.spring;

import haidnor.remoting.core.NettyRemotingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyServerStartup implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(NettyServerStartup.class);

    @Autowired
    private NettyRemotingServer nettyRemotingServer;

    @Override
    public void run(ApplicationArguments args) {
        nettyRemotingServer.start();
    }

}