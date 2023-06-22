package com.fullstack.Backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
        @NotEmpty
        private int deviceId;
        @NotNull
        @NotEmpty
        private String requester;
        @NotNull
        @NotEmpty
        private String nextKeeper;
        private Date bookingDate;
        private Date returnDate;
    }
}
