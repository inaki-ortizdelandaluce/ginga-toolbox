package org.ginga.toolbox.target.dao;

public class TargetDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public TargetDaoException() {
        super();
    }

    public TargetDaoException(String message) {
        super(message);
    }

    public TargetDaoException(Throwable cause) {
        super(cause);
    }

    public TargetDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TargetDaoException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
