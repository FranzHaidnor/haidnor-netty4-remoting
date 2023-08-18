package haidnor.remoting.core;

public class NettySystemConfig {
    public static final String REMOTING_NETTY_POOLED_BYTE_BUF_ALLOCATOR_ENABLE =
            "remoting.nettyPooledByteBufAllocatorEnable";
    public static final String REMOTING_SOCKET_SNDBUF_SIZE =
            "remoting.socket.sndbuf.size";
    public static final String REMOTING_SOCKET_RCVBUF_SIZE =
            "remoting.socket.rcvbuf.size";
    public static final String REMOTING_CLIENT_ASYNC_SEMAPHORE_VALUE =
            "remoting.clientAsyncSemaphoreValue";
    public static final String REMOTING_CLIENT_ONEWAY_SEMAPHORE_VALUE =
            "remoting.clientOnewaySemaphoreValue";
    public static final String REMOTING_CLIENT_WORKER_SIZE =
            "remoting.client.worker.size";
    public static final String REMOTING_CLIENT_CONNECT_TIMEOUT =
            "remoting.client.connect.timeout";
    public static final String REMOTING_CLIENT_CHANNEL_MAX_IDLE_SECONDS =
            "remoting.client.channel.maxIdleTimeSeconds";
    public static final String REMOTING_CLIENT_CLOSE_SOCKET_IF_TIMEOUT =
            "remoting.client.closeSocketIfTimeout";

    public static final boolean NETTY_POOLED_BYTE_BUF_ALLOCATOR_ENABLE = //
            Boolean.parseBoolean(System.getProperty(REMOTING_NETTY_POOLED_BYTE_BUF_ALLOCATOR_ENABLE, "false"));
    public static final int CLIENT_ASYNC_SEMAPHORE_VALUE = //
            Integer.parseInt(System.getProperty(REMOTING_CLIENT_ASYNC_SEMAPHORE_VALUE, "65535"));
    public static final int CLIENT_ONEWAY_SEMAPHORE_VALUE =
            Integer.parseInt(System.getProperty(REMOTING_CLIENT_ONEWAY_SEMAPHORE_VALUE, "65535"));
    public static int socketSndbufSize =
            Integer.parseInt(System.getProperty(REMOTING_SOCKET_SNDBUF_SIZE, "65535"));
    public static int socketRcvbufSize =
            Integer.parseInt(System.getProperty(REMOTING_SOCKET_RCVBUF_SIZE, "65535"));
    public static int clientWorkerSize =
            Integer.parseInt(System.getProperty(REMOTING_CLIENT_WORKER_SIZE, "4"));
    public static int connectTimeoutMillis =
            Integer.parseInt(System.getProperty(REMOTING_CLIENT_CONNECT_TIMEOUT, "3000"));
    public static int clientChannelMaxIdleTimeSeconds =
            Integer.parseInt(System.getProperty(REMOTING_CLIENT_CHANNEL_MAX_IDLE_SECONDS, "120"));
    public static boolean clientCloseSocketIfTimeout =
            Boolean.parseBoolean(System.getProperty(REMOTING_CLIENT_CLOSE_SOCKET_IF_TIMEOUT, "true"));
}
