package com.fullstack.Backend.services.impl;

import com.fullstack.Backend.dto.request.*;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.entities.KeeperOrder;
import com.fullstack.Backend.entities.Request;
import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.enums.RequestStatus;
import com.fullstack.Backend.enums.Status;
import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;
import com.fullstack.Backend.repositories.interfaces.IRequestRepository;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.request.ShowRequestsResponse;
import com.fullstack.Backend.responses.request.SubmitBookingResponse;
import com.fullstack.Backend.services.*;
import com.fullstack.Backend.utils.RequestFails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.*;

@Service
public class RequestService implements IRequestService {
    @Autowired
    IRequestRepository _requestRepository;
    @Autowired
    IEmployeeService _employeeService;
    @Autowired
    IKeeperOrderService _keeperOrderService;
    @Autowired
    IDeviceRepository _deviceRepository;

    @Override
    public CompletableFuture<SubmitBookingResponse> submitBookingRequest(SubmitBookingRequestDTO requests) throws InterruptedException, ExecutionException {
        List<RequestFails> requestFails = new ArrayList<RequestFails>();
        List<Request> requestSuccessful = new ArrayList<Request>();
        SubmitBookingResponse response = new SubmitBookingResponse();
        /* Were a list empty */
        if (requests.getRequestsList().size() == 0) {
            RequestFails requestFail = new RequestFails();
            requestFail.setErrorMessage("You didn't submit requests");
            requestFails.add(requestFail);
        }
        for (var request : requests.getRequestsList()) {
            RequestFails requestFail = new RequestFails();
            requestFail.setRequester(request.getRequester().trim());
            requestFail.setNextKeeper(request.getNextKeeper().trim());
            requestFail.setBookingDate(request.getBookingDate());
            requestFail.setReturnDate(request.getReturnDate());
            User requester = _employeeService.findByUsername(request.getRequester().trim()).get(),
                    nextKeeper = _employeeService.findByUsername(request.getNextKeeper().trim()).get();
            if (!_deviceRepository.existsById((long) request.getDeviceId())) {
                requestFail.setErrorMessage("The device you submitted is not existed");
                requestFails.add(requestFail);
                continue;
            }
            Device device = _deviceRepository.findById(request.getDeviceId());
            requestFail.setDeviceName(device.getName().trim());
            requestFail.setPlatformName(device.getPlatform().getName().trim());
            requestFail.setPlatformVersion(device.getPlatform().getVersion().trim());
            requestFail.setItemType(device.getItemType().getName().trim());
            requestFail.setRamSize(device.getRam().getSize().toString().trim());
            requestFail.setStorageSize(device.getStorage().getSize().toString().trim());
            requestFail.setScreenSize(device.getScreen().getSize().toString().trim());
            requestFail.setInventoryNumber(device.getInventoryNumber().trim());
            requestFail.setSerialNumber(device.getSerialNumber().trim());
            boolean isDeviceUsable = !device.getStatus().name().equalsIgnoreCase("broken") && !device.getStatus().name().equalsIgnoreCase("unavailable"),
                    isNextKeeperValid = nextKeeper != null && !request.getNextKeeper().trim().equalsIgnoreCase(device.getOwner().getUserName()),
                    isDateInvalid = request.getBookingDate() == null && request.getReturnDate() == null,
                    isRequesterInvalid = requester == null,
                    isBookingGreaterThanReturnDate = Objects.requireNonNull(request.getBookingDate()).before(Objects.requireNonNull(request.getReturnDate()));
            if (!isDeviceUsable) {
                requestFail.setErrorMessage("The device you submitted is unusable");
                requestFails.add(requestFail);
                continue;
            }
            if (isRequesterInvalid) {
                requestFail.setErrorMessage("The requester must be non-null");
                requestFails.add(requestFail);
                continue;
            }
            if (!isNextKeeperValid) {
                requestFail.setErrorMessage("The next keeper you submitted must be non-null or not identical to a device's owner");
                requestFails.add(requestFail);
                continue;
            }
            if (isDateInvalid) {
                requestFail.setErrorMessage("The dates you submitted must be non-null");
                requestFails.add(requestFail);
                continue;
            }
            if (!isBookingGreaterThanReturnDate) {
                requestFail.setErrorMessage("The booking date must be less than return date");
                requestFails.add(requestFail);
                continue;
            }
            String requestId = UUID.randomUUID().toString().replace("-", "");
            Request requestData = new Request();
            requestData.setRequestId(requestId);
            requestData.setRequester_Id(requester.getId());
            requestData.setNextKeeper_Id(nextKeeper.getId());
            requestData.setBookingDate(request.getBookingDate());
            requestData.setReturnDate(request.getReturnDate());
            requestData.setDevice_Id(device.getId());
            requestData.setCreatedDate(new Date());
            CompletableFuture<List<KeeperOrder>> keeperOrderList = _keeperOrderService.getKeeperOrderListByDeviceId(request.getDeviceId());
            boolean areRequestsIdentical = false;
            for (Request r : requestSuccessful) {
                if (r.getDevice_Id() == requestData.getDevice_Id() && r.getRequester_Id() == requestData.getRequester_Id() && r.getNextKeeper_Id() == requestData.getNextKeeper_Id()) {
                    areRequestsIdentical = true;
                    break;
                }
            }
            /* There are more than 2 identical requests when SUBMITTING */
            if (areRequestsIdentical) {
                requestFail.setErrorMessage("There are more than 2 identical requests when submitting");
                requestFails.add(requestFail);
                continue;
            }
            if(keeperOrderList.get().size() == 3){
                requestFail.setErrorMessage("Keeper number exceeds the allowance of times");
                requestFails.add(requestFail);
                continue;
            }
            /* The request whose device was never approved before */
            if (keeperOrderList.get().size() == 0) {
                User owner = _employeeService.findByUsername(device.getOwner().getUserName()).get();
                boolean isRequestRepetitive = _requestRepository.findRepetitiveRequest(requester.getId(), owner.getId(), nextKeeper.getId(), device.getId()) != null;
                /* The submitted request is already existent in the database (the current keeper is the owner)*/
                if (isRequestRepetitive) {
                    requestFail.setErrorMessage("The submitted request is already existent in the database");
                    requestFails.add(requestFail);
                    continue;
                }
                /* A keeper order's no = 0 and
                the OWNER of the booked device
                concurs the NEXT KEEPER to keep it
                */
                requestData.setAccepter_Id(owner.getId());
                requestData.setCurrentKeeper_Id(owner.getId());
                requestData.setRequestStatus(RequestStatus.values()[PENDING]);
                requestSuccessful.add(requestData);
            }

            /* The request whose device was approved before */
            for (KeeperOrder keeperOrder : keeperOrderList.get()) {
                boolean areDatesInDuration = request.getBookingDate().before(request.getReturnDate()) && request.getBookingDate().after(keeperOrder.getBookingDate()) && request.getReturnDate().before(keeperOrder.getDueDate());

                /* B borrowed A's
                   C borrowed B's
                   A and B cannot borrow C's
                */
                if (nextKeeper.getId() == keeperOrder.getKeeper().getId()) {
                    requestFail.setErrorMessage("The next keeper is existent in keeper order. Please try another next keeper");
                    requestFails.add(requestFail);
                    break;
                }
                /*  1: B borrowed A's from 1/7 - 1/10
                 *  2: C borrowed B's from 1/8 - 1/9
                 *  3: D borrowed C's from 2/8 - 15/8
                 *  Hence, Booking date and return date must be
                 *  in the valid date range of the latest keeper order.
                 * */
                if (!areDatesInDuration) {
                    requestFail.setErrorMessage("The booking date and/or return date are out of keeper order's date range");
                    requestFails.add(requestFail);
                    break;
                }

                boolean isRequestRepetitive = _requestRepository.findRepetitiveRequest(requester.getId(), keeperOrder.getKeeper().getId(), nextKeeper.getId(), device.getId()) != null;
                /* The submitted request is already existent in the database (the current keeper is not the owner) */
                if (isRequestRepetitive) {
                    requestFail.setErrorMessage("Your request is repetitive");
                    requestFails.add(requestFail);
                    break;
                }
                /* A keeper order's no > 0
                and someone keeping the booked device
                concurs the NEXT KEEPER to keep it
                */
                requestData.setAccepter_Id(keeperOrder.getKeeper().getId());
                requestData.setCurrentKeeper_Id(keeperOrder.getKeeper().getId());
                requestData.setRequestStatus(RequestStatus.values()[PENDING]);
                requestSuccessful.add(requestData);
            }
        }
        if (requestFails.size() > 0) {
            response.setFailedRequestsList(requestFails);
            return CompletableFuture.completedFuture(response);
        }
        for (Request request : requestSuccessful) _requestRepository.save(request);
        response.setFailedRequestsList(requestFails);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<ShowRequestsResponse> showRequestListsWithPaging(int employeeId, int pageIndex, int pageSize, String sortBy, String sortDir, RequestFilterDTO requestFilter)
            throws InterruptedException, ExecutionException {
        formatFilter(requestFilter);
        List<Request> requests = _requestRepository.findAllRequest(employeeId, sortBy, sortDir);
        List<String> requestStatusList = requests.stream().map(c -> c.getRequestStatus().name()).distinct().collect(Collectors.toList());
        requests = fetchFilteredRequest(requestFilter, requests).get();
        requests = getPage(requests, pageIndex, pageSize).get();
        List<RequestDTO> requestList = requests.stream().map(request -> new RequestDTO(request)).collect(Collectors.toList());
        ShowRequestsResponse response = new ShowRequestsResponse();
        response.setRequestsList(requestList);
        response.setPageNo(pageIndex);
        response.setPageSize(pageSize);
        response.setTotalElements(requests.size());
        response.setTotalPages(getTotalPages(pageSize, requests.size()));
        response.setRequestStatusList(requestStatusList);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public int getTotalPages(int pageSize, int listSize) {
        if (listSize == 0)
            return 1;

        if (listSize % pageSize == 0)
            return listSize / pageSize;

        return (listSize / pageSize) + 1;
    }

    @Override
    public void formatFilter(RequestFilterDTO requestFilter) {
        if (requestFilter.getRequester() != null)
            requestFilter.setRequester(requestFilter.getRequester().trim().toLowerCase());

        if (requestFilter.getCurrentKeeper() != null)
            requestFilter.setCurrentKeeper(requestFilter.getCurrentKeeper().trim().toLowerCase());

        if (requestFilter.getNextKeeper() != null)
            requestFilter.setNextKeeper(requestFilter.getNextKeeper().trim().toLowerCase());

        if (requestFilter.getDevice() != null)
            requestFilter.setDevice(requestFilter.getDevice().trim().toLowerCase());
    }

    @Override
    public CompletableFuture<List<Request>> getPage(List<Request> sourceList, int pageIndex, int pageSize) {
        if (pageSize <= 0 || pageIndex <= 0)
            throw new IllegalArgumentException("invalid page size: " + pageSize);

        int fromIndex = (pageIndex - 1) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex)
            return CompletableFuture.completedFuture(Collections.emptyList());

        return CompletableFuture.completedFuture(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));
    }

    @Override
    public CompletableFuture<List<Request>> fetchFilteredRequest(RequestFilterDTO requestFilter, List<Request> requests) {
        if (requestFilter.getRequestId() != null)
            requests = requests.stream().filter(request -> request.getRequestId().equals(requestFilter.getRequestId())).collect(Collectors.toList());

        if (requestFilter.getDevice() != null)
            requests = requests.stream().filter(request -> request.getDevice().getName().toLowerCase().equals(requestFilter.getDevice())).collect(Collectors.toList());

        if (requestFilter.getRequester() != null)
            requests = requests.stream().filter(request -> request.getRequester().getUserName().toLowerCase().equals(requestFilter.getRequester())).collect(Collectors.toList());

        if (requestFilter.getCurrentKeeper() != null)
            requests = requests.stream().filter(request -> request.getCurrentKeeper().getUserName().toLowerCase().equals(requestFilter.getCurrentKeeper())).collect(Collectors.toList());

        if (requestFilter.getNextKeeper() != null)
            requests = requests.stream().filter(request -> request.getNextKeeper().getUserName().toLowerCase().equals(requestFilter.getNextKeeper())).collect(Collectors.toList());

        if (requestFilter.getRequestStatus() != null)
            requests = requests.stream().filter(request -> request.getRequestStatus().name().equalsIgnoreCase(requestFilter.getRequestStatus())).collect(Collectors.toList());

        if (requestFilter.getBookingDate() != null)
            requests = requests.stream().filter(request -> request.getBookingDate().after(requestFilter.getBookingDate())).collect(Collectors.toList());

        if (requestFilter.getReturnDate() != null)
            requests = requests.stream().filter(request -> request.getReturnDate().before(requestFilter.getReturnDate())).collect(Collectors.toList());

        return CompletableFuture.completedFuture(requests);
    }

    @Override
    public CompletableFuture<KeywordSuggestionResponse> getSuggestKeywordRequests(int employeeId, int fieldColumn, String keyword, RequestFilterDTO requestFilter) throws InterruptedException, ExecutionException {
        Set<String> keywordList = new HashSet<>();
        List<Request> requests = _requestRepository.findAllRequest(employeeId, "Id", "ASC");
        formatFilter(requestFilter);
        requests = fetchFilteredRequest(requestFilter, requests).get();
        switch (fieldColumn) {
            case REQUEST_REQUEST_ID_COLUMN ->
                    keywordList = requests.stream().map(Request::getRequestId).filter(requestId -> requestId.contains(keyword.strip().toLowerCase())).collect(Collectors.toSet());
            case REQUEST_DEVICE_NAME_COLUMN ->
                    keywordList = requests.stream().filter(request -> request.getDevice().getName().toLowerCase().contains(keyword.strip().toLowerCase())).map(r -> r.getDevice().getName()).collect(Collectors.toSet());
            case REQUEST_REQUESTER_COLUMN ->
                    keywordList = requests.stream().filter(request -> request.getRequester().getUserName().toLowerCase().contains(keyword.strip().toLowerCase())).map(r -> r.getRequester().getUserName()).collect(Collectors.toSet());
            case REQUEST_CURRENT_KEEPER_COLUMN ->
                    keywordList = requests.stream().filter(request -> request.getCurrentKeeper().getUserName().toLowerCase().contains(keyword.strip().toLowerCase())).map(r -> r.getCurrentKeeper().getUserName()).collect(Collectors.toSet());
            case REQUEST_NEXT_KEEPER_COLUMN ->
                    keywordList = requests.stream().filter(request -> request.getNextKeeper().getUserName().toLowerCase().contains(keyword.strip().toLowerCase())).map(r -> r.getNextKeeper().getUserName()).collect(Collectors.toSet());
        }
        KeywordSuggestionResponse response = new KeywordSuggestionResponse();
        response.setKeywordList(keywordList);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> updateRequestStatus(UpdateStatusRequestDTO requestDTO) throws InterruptedException, ExecutionException {
        Optional<Request> request = _requestRepository.findById((long) requestDTO.getRequestId());
        /* If the request in the database does not exist */
        if (request == null) return CompletableFuture.completedFuture(new ResponseEntity<>(false, NOT_FOUND));
        /* If the SUBMITTED request's request status was IDENTICAL to that of request in the DATABASE
         *  and request status must NOT be EXTENDING
         * */
        if (RequestStatus.valueOf(request.get().getRequestStatus().toString()).ordinal() == requestDTO.getRequestStatus() && requestDTO.getRequestStatus() != EXTENDING)
            return CompletableFuture.completedFuture(new ResponseEntity<>(false, NOT_ACCEPTABLE));
        switch (requestDTO.getRequestStatus()) {
            case APPROVED -> {
                request.get().setRequestStatus(RequestStatus.APPROVED);
                request.get().setApprovalDate(new Date());
                _requestRepository.save(request.get());
                /* Change all related pending requests' status except the SUBMITTED request to CANCELLED */
                List<Request> relatedRequests = _requestRepository.findDeviceRelatedApprovedRequest(request.get().getId(), request.get().getCurrentKeeper_Id(), request.get().getDevice().getId(), PENDING);
                if (relatedRequests != null) {
                    for (Request relatedRequest : relatedRequests) {
                        relatedRequest.setRequestStatus(RequestStatus.CANCELLED);
                        relatedRequest.setCancelledDate(new Date());
                        _requestRepository.save(relatedRequest);
                    }
                }
                /* Change device status to OCCUPIED when a request is approved */
                Device device = _deviceRepository.findById(request.get().getDevice().getId());
                if(device.getStatus() != Status.OCCUPIED){
                    device.setStatus(Status.OCCUPIED);
                    _deviceRepository.save(device);
                }
            }
            case REJECTED -> {
                request.get().setRequestStatus(RequestStatus.REJECTED);
                request.get().setApprovalDate(new Date());
                _requestRepository.save(request.get());
            }
            case TRANSFERRED -> {
                request.get().setRequestStatus(RequestStatus.TRANSFERRED);
                request.get().setTransferredDate(new Date());
                _requestRepository.save(request.get());
                List<KeeperOrder> keeperOrderList = _keeperOrderService.getKeeperOrderListByDeviceId(request.get().getDevice().getId()).get();
                int keeperNo = 0;
                KeeperOrder keeperOrder = new KeeperOrder();
                if (keeperOrderList.size() != 0) /* Get the latest keeper order's number */
                    keeperNo = keeperOrderList.stream().max(Comparator.comparing(k -> k.getKeeperNo())).map(k -> k.getKeeperNo()).get();
                keeperOrder.setDevice(request.get().getDevice());
                keeperOrder.setKeeper(request.get().getNextKeeper());
                keeperOrder.setKeeperNo(keeperNo + 1);  /* By virtue of being a new keeper order, keeperNo is increased */
                keeperOrder.setBookingDate(request.get().getBookingDate());
                keeperOrder.setDueDate(request.get().getReturnDate());
                keeperOrder.setIsReturned(false);
                keeperOrder.setCreatedDate(new Date());
                keeperOrder.setUpdatedDate(new Date());
                _keeperOrderService.createKeeperOrder(keeperOrder);
            }
            /* Test in case:
             * Keeper order = 1
             * Keeper order = 2 or 3
             * */
            case EXTENDING -> {
                /* The request will change its status and update approval date */
                request.get().setRequestStatus(RequestStatus.TRANSFERRED); /* ? */
                request.get().setApprovalDate(new Date());
                _requestRepository.save(request.get());
                /* The OLD request will change its status to CANCELLED */
                List<Request> preExtendDurationRequest = _requestRepository.findDeviceRelatedApprovedRequest(request.get().getId(), request.get().getCurrentKeeper_Id(), request.get().getDevice().getId(), TRANSFERRED);
                if (preExtendDurationRequest != null) {
                    for (Request relatedRequest : preExtendDurationRequest) {
                        relatedRequest.setRequestStatus(RequestStatus.CANCELLED);
                        relatedRequest.setCancelledDate(new Date());
                        _requestRepository.save(relatedRequest);
                    }
                }
                /* UPDATE order's return date */
                KeeperOrder updatedKeeperOrder = _keeperOrderService.findKeeperOrderByDeviceIdAndKeeperId(
                        request.get().getDevice().getId(),
                        request.get().getNextKeeper_Id()).get();
                updatedKeeperOrder.setDueDate(request.get().getReturnDate());
                updatedKeeperOrder.setUpdatedDate(new Date());
                _keeperOrderService.updateKeeperOrder(updatedKeeperOrder);
            }
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(true, OK));
    }

    @Override
    public CompletableFuture<ResponseEntity<Object>> extendDurationRequest(ExtendDurationRequestDTO request) throws InterruptedException, ExecutionException, ParseException {
        /*
         *  Find the current request via next keeper, device and status
         *  Find the previous order to have the max duration for the device
         *  Create a new request for sending requests to the person accepting reviews it
         *
         * */
        CompletableFuture<User> nextKeeper = _employeeService.findByUsername(request.getNextKeeper());
        Device device = _deviceRepository.findById(request.getDeviceId());
        if (request.getReturnDate() == null)
            return CompletableFuture.completedFuture(new ResponseEntity<>("Return date must not be empty!", NOT_ACCEPTABLE));
        if (nextKeeper.get() == null)
            return CompletableFuture.completedFuture(new ResponseEntity<>("Next keeper is not existent", NOT_FOUND));
        if (device == null)
            return CompletableFuture.completedFuture(new ResponseEntity<>("Device is not existent", NOT_FOUND));
        Request r = findAnOccupiedRequest(nextKeeper.get().getId(), device.getId()).get();
        if (r == null)
            return CompletableFuture.completedFuture(new ResponseEntity<>("Request is not existent", NOT_FOUND));
        if (request.getReturnDate().before(r.getReturnDate()))
            return CompletableFuture.completedFuture(new ResponseEntity<>("Return date must be after than the available current return date!", NOT_FOUND));

        /* Find the previous order to extend duration and validate the return date */
        List<KeeperOrder> keeperOrderList = _keeperOrderService.getKeeperOrderListByDeviceId(device.getId()).get();
        KeeperOrder currentKeeperOrder = new KeeperOrder();
        KeeperOrder previousKeeperOrder;
        for (KeeperOrder k : keeperOrderList) {
            if (k.getKeeper().getId() == nextKeeper.get().getId())
                currentKeeperOrder = k;
        }
        if (currentKeeperOrder == null)
            return CompletableFuture.completedFuture(new ResponseEntity<>("Request is not approved", NOT_FOUND));
        if (currentKeeperOrder.getKeeperNo() > 1) {
            int currentOrder = currentKeeperOrder.getKeeperNo();
            previousKeeperOrder = keeperOrderList.stream().filter(k -> k.getKeeperNo() == currentOrder - 1).findFirst().get();
            if (request.getReturnDate().after(previousKeeperOrder.getDueDate()))
                return CompletableFuture.completedFuture(new ResponseEntity<>("Return date exceeds the allowed duration!", NOT_ACCEPTABLE));
        }
        /* */
        Request postExtendDurationRequest = new Request();
        postExtendDurationRequest.setRequester_Id(r.getRequester_Id());
        postExtendDurationRequest.setRequestId(r.getRequestId());
        postExtendDurationRequest.setCurrentKeeper_Id(r.getCurrentKeeper_Id());
        postExtendDurationRequest.setNextKeeper_Id(nextKeeper.get().getId());
        postExtendDurationRequest.setBookingDate(r.getBookingDate());
        postExtendDurationRequest.setReturnDate(request.getReturnDate());
        postExtendDurationRequest.setDevice_Id(r.getDevice_Id());
        postExtendDurationRequest.setCreatedDate(r.getCreatedDate());
        postExtendDurationRequest.setUpdatedDate(new Date());
        postExtendDurationRequest.setAccepter_Id(r.getAccepter_Id());
        postExtendDurationRequest.setTransferredDate(r.getTransferredDate());
        postExtendDurationRequest.setRequestStatus(RequestStatus.values()[EXTENDING]);
        _requestRepository.save(postExtendDurationRequest);
        return CompletableFuture.completedFuture(new ResponseEntity<>("Send request successfully", OK));
    }

    @Override
    public CompletableFuture<Request> findAnOccupiedRequest(int nextKeeperId, int deviceId) throws InterruptedException, ExecutionException, ParseException {
        return  CompletableFuture.completedFuture(_requestRepository.findAnOccupiedRequest(nextKeeperId, deviceId));
    }

    @Override
    public void updateRequest(Request request) throws InterruptedException, ExecutionException {
        _requestRepository.save(request);
    }
}
