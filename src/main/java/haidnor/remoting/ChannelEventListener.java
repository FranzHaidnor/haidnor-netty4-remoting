package haidnor.remoting;

import io.netty.channel.Channel;

public interface ChannelEventListener {
    default void onChannelConnect(final String remoteAddr, final Channel channel) {
    }

    default void onChannelClose(final String remoteAddr, final Channel channel) {
    }

    default void onChannelException(final String remoteAddr, final Channel channel) {
    }

    default void onChannelIdle(final String remoteAddr, final Channel channel) {
    }
}
