package org.ginga.tools.lacdump.dao;

public class LacDumpDaoException extends Exception {

    private static final long serialVersionUID = 1L;

    public LacDumpDaoException() {
        super();
    }

    public LacDumpDaoException(String message) {
        super(message);
    }

    public LacDumpDaoException(Throwable cause) {
        super(cause);
    }

    public LacDumpDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public LacDumpDaoException(String message, Throwable cause, boolean enableSupression,
            boolean writableStackTrace) {
        super(message, cause, enableSupression, writableStackTrace);
    }

}
