
package com.scrambleticket.exception;

// TODO wait check
public class TicketExceedException extends ScrambleTicketException {

    public TicketExceedException(String message) {
        super(message);
    }

    public TicketExceedException(String message, Throwable cause) {
        super(message, cause);
    }

}
