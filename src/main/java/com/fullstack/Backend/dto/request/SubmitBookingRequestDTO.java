package com.fullstack.Backend.dto.request;

import com.fullstack.Backend.entities.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubmitBookingRequestDTO {
    List<RequestInput> requestsList;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestInput {
        private int deviceId;

        private String requester;

        private String nextKeeper;

        private Date bookingDate;

        private Date returnDate;
    }
}
