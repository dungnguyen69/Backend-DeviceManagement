package com.fullstack.Backend.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
	VACANT, UNAVAILABLE, OCCUPIED, BROKEN;
	public static Optional<Status> findByNumber(int number) {
		return Arrays.stream(Status.values())
				.filter(e -> e.ordinal() == number)
				.findFirst();
	}
}
