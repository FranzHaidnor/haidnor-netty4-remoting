package haidnor.remoting.core;

public enum NettyEventType {
    CONNECT,
    CLOSE,
    READER_IDLE,
    WRITER_IDLE,
    ALL_IDLE,
    EXCEPTION
}
