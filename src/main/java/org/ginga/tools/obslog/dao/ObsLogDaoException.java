package org.ginga.tools.obslog.dao;

public class ObsLogDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ObsLogDaoException() {
        super();
    }

    public ObsLogDaoException(String message) {
        super(message);
    }

    public ObsLogDaoException(Throwable cause) {
        super(cause);
    }

    public ObsLogDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObsLogDaoException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
