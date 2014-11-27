package org.ginga.tools.obslog.dao;

public class ObslogDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ObslogDaoException() {
        super();
    }

    public ObslogDaoException(String message) {
        super(message);
    }

    public ObslogDaoException(Throwable cause) {
        super(cause);
    }

    public ObslogDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObslogDaoException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
