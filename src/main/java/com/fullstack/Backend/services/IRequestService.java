package com.fullstack.Backend.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fullstack.Backend.dto.request.RequestFilterDTO;
import com.fullstack.Backend.dto.request.SubmitBookingRequestDTO;
import com.fullstack.Backend.entities.Request;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.request.ShowRequestsResponse;
import com.fullstack.Backend.responses.request.SubmitBookingResponse;

public interface IRequestService {
    public CompletableFuture<SubmitBookingResponse> submitBookingRequest(SubmitBookingRequestDTO requests)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<ShowRequestsResponse> showRequestListsWithPaging(int employeeId, int pageIndex, int pageSize,
                                                                              String sortBy, String sortDir, RequestFilterDTO requestFilter)
            throws InterruptedException, ExecutionException;

    public int getTotalPages(int pageSize, int listSize);

    public void formatFilter(RequestFilterDTO requestFilter);

    public CompletableFuture<List<Request>> getPage(List<Request> sourceList, int pageIndex, int pageSize);

    public CompletableFuture<List<Request>> fetchFilteredRequest(RequestFilterDTO requestFilter, List<Request> requests);

    public CompletableFuture<KeywordSuggestionResponse> getSuggestKeywordRequests(int employeeId, int fieldColumn, String keyword,
                                                                                  RequestFilterDTO requestFilter) throws InterruptedException, ExecutionException;
}
