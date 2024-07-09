
package com.scrambleticket;

public class ScrambleTicketException extends RuntimeException {

    public ScrambleTicketException(String message) {
        super(message);
    }

    public ScrambleTicketException(String message, Throwable cause) {
        super(message, cause);
    }

}
