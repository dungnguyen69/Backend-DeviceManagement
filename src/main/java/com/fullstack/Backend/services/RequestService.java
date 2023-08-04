package com.fullstack.Backend.services;

import java.text.ParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fullstack.Backend.dto.request.ExtendDurationRequestDTO;
import com.fullstack.Backend.dto.request.RequestFilterDTO;
import com.fullstack.Backend.dto.request.SubmitBookingRequestDTO;
import com.fullstack.Backend.dto.request.UpdateStatusRequestDTO;
import com.fullstack.Backend.entities.Request;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.request.SubmitBookingResponse;
import org.springframework.http.ResponseEntity;

public interface RequestService {
    public CompletableFuture<SubmitBookingResponse> submitBookingRequest(SubmitBookingRequestDTO requests)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> showRequestListsWithPaging(int employeeId, int pageIndex, int pageSize,
                                                                                String sortBy, String sortDir, RequestFilterDTO requestFilter)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<KeywordSuggestionResponse> getSuggestKeywordRequests(int employeeId, int fieldColumn, String keyword,
                                                                                  RequestFilterDTO requestFilter) throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> updateRequestStatus(UpdateStatusRequestDTO updateStatusRequestDTO) throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> extendDurationRequest(ExtendDurationRequestDTO request) throws InterruptedException, ExecutionException, ParseException;

    public CompletableFuture<Request> findAnOccupiedRequest(int nextKeeperId, int deviceId) throws InterruptedException, ExecutionException, ParseException;

    public void updateRequest(Request request) throws InterruptedException, ExecutionException;

    public boolean findRequestBasedOnStatusAndDevice(int deviceId, int requestStatus);

    public void deleteRequestBasedOnStatusAndDevice(int deviceId, int requestStatus);

}
