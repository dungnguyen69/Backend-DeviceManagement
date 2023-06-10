package com.fullstack.Backend.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Origin {
    Internal, External;

    public static Optional<Origin> findByNumber(int number) {
        return Arrays.stream(Origin.values())
                .filter(e -> e.ordinal() == number)
                .findFirst();
    }
}
