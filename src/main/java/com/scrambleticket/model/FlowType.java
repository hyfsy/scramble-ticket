
package com.scrambleticket.model;

import static com.scrambleticket.model.FlowType.Constants.*;

public enum FlowType {

    // 1 1 1
    // common candidate pay

    COMMON(1, COMMON_MASK), //
    CANDIDATE(2, CANDIDATE_MASK), //
    COMMON_CANDIDATE(3, COMMON_MASK + CANDIDATE_MASK), //
    COMMON_PAY(4, COMMON_MASK + PAY_MASK), //
    CANDIDATE_PAY(5, CANDIDATE_MASK + PAY_MASK), //
    COMMON_CANDIDATE_PAY(6, COMMON_MASK + CANDIDATE_MASK + PAY_MASK), //
    ;

    private final int code;
    private final int mask;

    FlowType(int code, int mask) {
        this.code = code;
        this.mask = mask;
    }

    public static FlowType getByCode(int code) {
        for (FlowType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new UnsupportedOperationException("code: " + code);
    }

    public boolean isCommon() {
        return (mask & COMMON_MASK) == COMMON_MASK;
    }

    public boolean isCandidate() {
        return (mask & CANDIDATE_MASK) == CANDIDATE_MASK;
    }

    public boolean isPay() {
        return (mask & PAY_MASK) == PAY_MASK;
    }

    static class Constants {
        public static final int COMMON_MASK = 1 << 2;
        public static final int CANDIDATE_MASK = 1 << 1;
        public static final int PAY_MASK = 1;
    }
}
