package haidnor.remoting.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NettyServerConfig {
    private int listenPort = 8080;
    private int serverWorkerThreads = 8;
    private int serverCallbackExecutorThreads = 0;
    private int serverSelectorThreads = 3;
    private int serverOnewaySemaphoreValue = 256;
    private int serverAsyncSemaphoreValue = 64;
    private int serverChannelMaxIdleTimeSeconds = 120;
    private int serverSocketSndBufSize = 65535;
    private int serverSocketRcvBufSize = 65535;
    private boolean serverPooledByteBufAllocatorEnable = true;
    /**
     * make make install
     * <p>
     * ../glibc-2.10.1/configure \ --prefix=/usr \ --with-headers=/usr/include \
     * --host=x86_64-linux-gnu \ --build=x86_64-pc-linux-gnu \ --without-gd
     */
    private boolean useEpollNativeSelector = false;

}
