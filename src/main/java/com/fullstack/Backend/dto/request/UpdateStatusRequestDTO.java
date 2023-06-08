package com.fullstack.Backend.dto.request;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateStatusRequestDTO {
    private int requestId;
    private int requestStatus;
}
