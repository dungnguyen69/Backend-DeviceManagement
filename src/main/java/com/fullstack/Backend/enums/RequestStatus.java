package com.fullstack.Backend.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestStatus {
	APPROVED(0), CANCELLED(2), TRANSFERRED(3), PENDING(4), RETURNED(5), EXTENDING(6);

	private final int id;
	RequestStatus(int id) {
		this.id = id;
	}

	public static Optional<RequestStatus> fromNumber(int text) {
		return Arrays.stream(values())
				.filter(e -> e.id == text)
				.findFirst();
	}
}
