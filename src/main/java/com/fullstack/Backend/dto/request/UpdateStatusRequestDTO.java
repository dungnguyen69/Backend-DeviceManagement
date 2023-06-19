package com.fullstack.Backend.dto.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateStatusRequestDTO {
    @NotNull
    @NotEmpty
    private int requestId;
    @NotNull
    @NotEmpty
    private int requestStatus;
}
