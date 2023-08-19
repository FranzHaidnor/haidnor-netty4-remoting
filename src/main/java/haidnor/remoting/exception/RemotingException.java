package haidnor.remoting.exception;

import java.io.Serial;

public class RemotingException extends Exception {
    @Serial
    private static final long serialVersionUID = -5690687334570505110L;

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }
}
