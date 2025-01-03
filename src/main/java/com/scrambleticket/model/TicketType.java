
package com.scrambleticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketType {
    AUDIT("1", "成人"), //
    CHILD("2", "儿童"), //
    STUDENT("3", "学生"), //
    DISABILITY("4", "军人"), //
    ;

    final String code;
    final String name;

    public static TicketType getByCode(String code) {
        for (TicketType ticketType : values()) {
            if (ticketType.code.equals(code)) {
                return ticketType;
            }
        }
        throw new IllegalArgumentException("unknown code: " + code);
    }
}
