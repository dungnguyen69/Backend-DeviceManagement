package com.fullstack.Backend.responses.request;

import com.fullstack.Backend.dto.request.RequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowRequestsResponse {
    List<RequestDTO> requestsList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private List<String> requestStatusList;
}
