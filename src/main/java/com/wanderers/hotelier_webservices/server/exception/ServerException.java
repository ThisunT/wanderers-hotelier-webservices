package com.wanderers.hotelier_webservices.server.exception;

public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Class constructor
     *
     * @param message String value of Exception
     */
    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable t) {
        super(message, t);
    }
}
