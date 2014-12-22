package org.ginga.toolbox.lacdump.dao;

public class LacdumpDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public LacdumpDaoException() {
        super();
    }

    public LacdumpDaoException(String message) {
        super(message);
    }

    public LacdumpDaoException(Throwable cause) {
        super(cause);
    }

    public LacdumpDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public LacdumpDaoException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
