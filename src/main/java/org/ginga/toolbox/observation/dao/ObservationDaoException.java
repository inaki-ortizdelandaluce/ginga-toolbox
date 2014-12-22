package org.ginga.toolbox.observation.dao;

public class ObservationDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public ObservationDaoException() {
        super();
    }

    public ObservationDaoException(String message) {
        super(message);
    }

    public ObservationDaoException(Throwable cause) {
        super(cause);
    }

    public ObservationDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObservationDaoException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
