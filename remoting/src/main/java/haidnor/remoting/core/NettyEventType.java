package haidnor.remoting.core;

public enum NettyEventType {

    // Override ChannelOutboundHandler ------------------------------------------------------------------------------
    // 主动发起连接 (不代表连接成功)
    OUT_BOUND_CONNECT,
    OUT_BOUND_DISCONNECT,
    // 自身主动关闭连接时触发
    OUT_BOUND_CLOSE,

    // Override ChannelInboundHandler ------------------------------------------------------------------------------
    // 通道状态变为活动状态 (连接成功时触发)
    IN_BOUND_ACTIVE,
    // 通道状态变为非活动状态 (连接断开时触发)
    IN_BOUND_INACTIVE,
    // 网络连接中断,数据格式错误,处理逻辑异常等场景触发
    IN_BOUND_EXCEPTION_CAUGHT,

    // 读空闲
    IN_BOUND_READER_IDLE,
    // 写空闲
    IN_BOUND_WRITER_IDLE,
    // 读写空闲
    IN_BOUND_ALL_IDLE
}
