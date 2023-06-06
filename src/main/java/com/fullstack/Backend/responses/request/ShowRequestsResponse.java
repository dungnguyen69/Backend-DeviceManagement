package com.fullstack.Backend.responses.request;

import com.fullstack.Backend.entities.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowRequestsResponse {
    List<Request> requestsList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private List<String> requestStatusList;
}
