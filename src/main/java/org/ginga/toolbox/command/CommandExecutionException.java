package org.ginga.toolbox.command;

public class CommandExecutionException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommandExecutionException() {
        super();
    }

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(Throwable cause) {
        super(cause);
    }

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExecutionException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
