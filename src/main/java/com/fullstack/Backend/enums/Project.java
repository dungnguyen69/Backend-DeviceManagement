package com.fullstack.Backend.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Project {
    NOKIA, TELSA, MERCEDES, BMW;

    public static Optional<Project> findByNumber(int number) {
        return Arrays.stream(Project.values())
                .filter(e -> e.ordinal() == number)
                .findFirst();
    }
}
