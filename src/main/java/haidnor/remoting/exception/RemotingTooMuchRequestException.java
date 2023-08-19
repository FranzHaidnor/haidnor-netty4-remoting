package haidnor.remoting.exception;

import java.io.Serial;

public class RemotingTooMuchRequestException extends RemotingException {
    @Serial
    private static final long serialVersionUID = 4326919581254519654L;

    public RemotingTooMuchRequestException(String message) {
        super(message);
    }
}
