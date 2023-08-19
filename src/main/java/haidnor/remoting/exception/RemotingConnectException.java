package haidnor.remoting.exception;

import java.io.Serial;

public class RemotingConnectException extends RemotingException {

    @Serial
    private static final long serialVersionUID = -5565366231695911316L;

    public RemotingConnectException(String addr) {
        this(addr, null);
    }

    public RemotingConnectException(String addr, Throwable cause) {
        super("connect to " + addr + " failed", cause);
    }
}
