package com.fullstack.Backend.controllers;

import com.fullstack.Backend.dto.request.*;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.request.SubmitBookingResponse;
import com.fullstack.Backend.services.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    @Autowired
    RequestService _requestService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getRequestsWithPaging(
            @PathVariable(value = "id") int employeeId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) RequestFilterDTO requestFilterDTO) throws InterruptedException, ExecutionException {
        return _requestService.showRequestListsWithPaging(employeeId, pageNo, pageSize, sortBy, sortDir, requestFilterDTO);
    }

    @PostMapping("/submissions")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> submitBookingRequest(
            @Valid @RequestBody SubmitBookingRequestDTO requests)
            throws InterruptedException, ExecutionException {
        CompletableFuture<SubmitBookingResponse> response = _requestService.submitBookingRequest(requests);
        return new ResponseEntity<>(response.get(), OK);
    }

    @GetMapping("/suggestions/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> getSuggestKeywordRequests(
            @PathVariable(value = "id") int employeeId,
            @RequestParam(name = "column") int fieldColumn,
            @RequestParam(name = "keyword") String keyword, RequestFilterDTO request)
            throws InterruptedException, ExecutionException {
        if (keyword.trim().isBlank())
            return ResponseEntity.status(NOT_FOUND).body("Keyword must be non-null");
        CompletableFuture<KeywordSuggestionResponse> response = _requestService.getSuggestKeywordRequests(employeeId, fieldColumn, keyword, request);
        return new ResponseEntity<>(response.get(), OK);
    }

    @PostMapping("/status-update")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> updateRequestStatus(@RequestBody UpdateStatusRequestDTO request)
            throws InterruptedException, ExecutionException {
        return _requestService.updateRequestStatus(request);
    }

    @PostMapping("/keepers/extend-duration")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> extendDurationRequest(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestBody ExtendDurationRequestDTO request)
            throws InterruptedException, ExecutionException, ParseException {
        return _requestService.extendDurationRequest(request);
    }
}
