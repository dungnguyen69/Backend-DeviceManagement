package com.fullstack.Backend.dto.request;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateStatusRequestDTO {
    @NotEmpty
    private int requestId;
    @NotEmpty
    private int requestStatus;
}
