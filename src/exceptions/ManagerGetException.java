package exceptions;

import java.io.IOException;

public class ManagerGetException extends IOException {
    public ManagerGetException(String message) {
        super(message);
    }
}
