package haidnor.remoting.exception;

import java.io.Serial;

public class RemotingSendRequestException extends RemotingException {
    @Serial
    private static final long serialVersionUID = 5391285827332471674L;

    public RemotingSendRequestException(String addr) {
        this(addr, null);
    }

    public RemotingSendRequestException(String addr, Throwable cause) {
        super("send request to <" + addr + "> failed", cause);
    }
}
