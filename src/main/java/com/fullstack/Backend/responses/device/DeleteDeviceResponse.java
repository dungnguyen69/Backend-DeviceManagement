package com.fullstack.Backend.responses.device;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteDeviceResponse {
	private Boolean isDeletionSuccessful = false;
	private String errorMessage;
}
