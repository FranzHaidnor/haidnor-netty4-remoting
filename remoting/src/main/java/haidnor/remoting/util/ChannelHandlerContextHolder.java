package haidnor.remoting.util;

import io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerContextHolder {

    private static final ThreadLocal<ChannelHandlerContext> holder = new ThreadLocal();

    public static void set(ChannelHandlerContext channelHandlerContext) {
        holder.set(channelHandlerContext);
    }

    public static ChannelHandlerContext get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }

}
