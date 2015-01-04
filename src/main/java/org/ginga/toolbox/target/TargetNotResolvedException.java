package org.ginga.toolbox.target;

public class TargetNotResolvedException extends Exception {

    private static final long serialVersionUID = 1L;

    public TargetNotResolvedException() {
        super();
    }

    public TargetNotResolvedException(String target) {
        super("Target " + target + " could not be resolved");
    }

    public TargetNotResolvedException(Throwable cause) {
        super(cause);
    }

    public TargetNotResolvedException(String target, Throwable cause) {
        super("Target " + target + " could not be resolved", cause);
    }

    public TargetNotResolvedException(String target, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super("Target " + target + " could not be resolved", cause, enableSupression,
                writableStackTrace);
    }

}
