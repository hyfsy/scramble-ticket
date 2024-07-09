
package com.scrambleticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatType {
    // TODO 类别
    // TDZ("P", "特等座"), //
    SWZ("9", "商务座"), //
    // GJDW("A", "高级动卧"), //
    YDZ("M", "一等座"), //
    EDZ("O", "二等座"), //
    GJRW("6", "高级软卧"), //
    // YDW("I", "一等卧"), //
    // EDW("J", "二等卧"), //
    RW("4", "软卧"), //
    YW("3", "硬卧"), //
    // DW("F", "动卧"), //
    RZ("2", "软座"), //
    YZ("1", "硬座"), //
    QT("H", "其他"), //
    // TODO ?
    // WZ("WZ", "无座"), //
    // W("W", "无座"), //
    ;

    final String code;
    final String name;

    public static SeatType getByCode(String code) {
        for (SeatType seatType : values()) {
            if (seatType.code.equals(code)) {
                return seatType;
            }
        }
        throw new IllegalArgumentException("unknown code: " + code);
    }
}
