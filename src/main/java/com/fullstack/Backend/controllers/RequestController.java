package com.fullstack.Backend.controllers;

import com.fullstack.Backend.dto.request.RequestFilterDTO;
import com.fullstack.Backend.dto.request.SubmitBookingRequestDTO;
import com.fullstack.Backend.dto.request.UpdateStatusRequestDTO;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.request.ShowRequestsResponse;
import com.fullstack.Backend.responses.request.SubmitBookingResponse;
import com.fullstack.Backend.services.IRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    @Autowired
    IRequestService _requestService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestsWithPaging(
            @PathVariable(value = "id") int employeeId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @DateTimeFormat(pattern = "yyyy-MM-dd") RequestFilterDTO requestFilterDTO) throws InterruptedException, ExecutionException {
        CompletableFuture<ShowRequestsResponse> response = _requestService.showRequestListsWithPaging(employeeId, pageNo,
                pageSize, sortBy, sortDir, requestFilterDTO);
        if (response.get().getTotalElements() != EMPTY_LIST)
            return new ResponseEntity<>(response.get(), OK);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @PostMapping("/submissions")
    @ResponseBody
    public ResponseEntity<Object> submitBookingRequest(
            @Valid @RequestBody SubmitBookingRequestDTO requests)
            throws InterruptedException, ExecutionException {
        CompletableFuture<SubmitBookingResponse> response = _requestService.submitBookingRequest(requests);
        return new ResponseEntity<>(response.get(), OK);
    }

    @GetMapping("/suggestions/{id}")
    @ResponseBody
    public ResponseEntity<Object> getSuggestKeywordRequests(@PathVariable(value = "id") int employeeId,
                                                            @RequestParam(name = "column") int fieldColumn,
                                                            @RequestParam(name = "keyword") String keyword, RequestFilterDTO request)
            throws InterruptedException, ExecutionException {
        if (keyword.trim().isBlank())
            return ResponseEntity.status(NOT_FOUND).body("Keyword must be non-null");

        CompletableFuture<KeywordSuggestionResponse> response = _requestService.getSuggestKeywordRequests(employeeId, fieldColumn,
                keyword, request);
        return new ResponseEntity<>(response.get(), OK);
    }

    @PutMapping("/status-update")
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordRequests(UpdateStatusRequestDTO request)
            throws InterruptedException, ExecutionException {
        return _requestService.updateRequestStatus(request);
    }
}
