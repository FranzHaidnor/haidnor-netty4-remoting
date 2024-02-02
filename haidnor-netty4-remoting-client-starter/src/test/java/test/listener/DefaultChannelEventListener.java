package test.listener;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingChannelEventListener;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@NettyRemotingChannelEventListener
@Component
public class DefaultChannelEventListener implements ChannelEventListener {

    private static final Logger log = LoggerFactory.getLogger(DefaultChannelEventListener.class);

    @Override
    public void onOutBoundConnect(String remoteAddr, Channel channel) {
        log.info("onOutBoundConnect");
    }

    @Override
    public void onOutBoundDisconnect(String remoteAddr, Channel channel) {
        log.info("onOutBoundDisconnect");
    }

    @Override
    public void onOutBoundClose(String remoteAddr, Channel channel) {
        log.info("onOutBoundClose");
    }

    @Override
    public void onInBoundActive(String remoteAddr, Channel channel) {
        log.info("onInBoundActive");
//        RemotingUtil.closeChannel(channel);
    }

    @Override
    public void onInBoundInactive(String remoteAddr, Channel channel) {
        log.info("onInBoundInactive");
    }

    @Override
    public void onInBoundExceptionCaught(String remoteAddr, Channel channel) {
        log.info("onInBoundExceptionCaught");
    }

    @Override
    public void onInBoundReaderIdle(String remoteAddr, Channel channel) {
        log.info("onInBoundReaderIdle");
    }

    @Override
    public void onInBoundWriterIdle(String remoteAddr, Channel channel) {
        log.info("onInBoundWriterIdle");
    }

    @Override
    public void onInBoundAllIdle(String remoteAddr, Channel channel) {
        log.info("onInBoundAllIdle");
    }

}
