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
        ChannelEventListener.super.onOutBoundConnect(remoteAddr, channel);
    }

    @Override
    public void onOutBoundDisconnect(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onOutBoundDisconnect(remoteAddr, channel);
    }

    @Override
    public void onOutBoundClose(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onOutBoundClose(remoteAddr, channel);
    }

    @Override
    public void onInBoundActive(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onInBoundActive(remoteAddr, channel);
    }

    @Override
    public void onInBoundInactive(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onInBoundInactive(remoteAddr, channel);
    }

    @Override
    public void onInBoundExceptionCaught(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onInBoundExceptionCaught(remoteAddr, channel);
    }

    @Override
    public void onInBoundReaderIdle(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onInBoundReaderIdle(remoteAddr, channel);
    }

    @Override
    public void onInBoundWriterIdle(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onInBoundWriterIdle(remoteAddr, channel);
    }

    @Override
    public void onInBoundAllIdle(String remoteAddr, Channel channel) {
        ChannelEventListener.super.onInBoundAllIdle(remoteAddr, channel);
    }

}
