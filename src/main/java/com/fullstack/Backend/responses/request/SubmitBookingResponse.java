package com.fullstack.Backend.responses.request;

import com.fullstack.Backend.utils.RequestFails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubmitBookingResponse {
    List<RequestFails> failedRequestsList;
}
