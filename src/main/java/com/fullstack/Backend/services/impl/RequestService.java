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
import com.fullstack.Backend.responses.users.MessageResponse;
import com.fullstack.Backend.services.*;
import com.fullstack.Backend.utils.RequestFails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.*;

@Service
public class RequestService implements IRequestService {
    @Autowired
    IRequestRepository _requestRepository;
    @Autowired
    IUserService _employeeService;
    @Autowired
    IKeeperOrderService _keeperOrderService;
    @Autowired
    IDeviceRepository _deviceRepository;

    @Async
    @Override
    public CompletableFuture<SubmitBookingResponse> submitBookingRequest(SubmitBookingRequestDTO requests) throws InterruptedException, ExecutionException {
        List<RequestFails> requestFails = new ArrayList<>();
        List<Request> requestSuccessful = new ArrayList<>();
        SubmitBookingResponse response = new SubmitBookingResponse();
        checkAnEmptyList(requests, requestFails);
        for (var submittedRequest : requests.getRequestsList()) {
            RequestFails requestFail = new RequestFails();
            List<String> error = new ArrayList<>();
            User requester = _employeeService.findByUsername(submittedRequest.getRequester().trim()).get(),
                    nextKeeper = _employeeService.findByUsername(submittedRequest.getNextKeeper().trim()).get();

            if (!_deviceRepository.existsById((long) submittedRequest.getDeviceId())) {
                error.add("The device you submitted is not existed");
                addToRequestFails(requestFails, requestFail, error);
                continue;
            }

            Device device = _deviceRepository.findById(submittedRequest.getDeviceId());
            setUpRequestFail(requestFail, submittedRequest, device);
            User owner = _employeeService.findByUsername(device.getOwner().getUserName()).get();
            validateRequestInput(error, device, requester, nextKeeper, submittedRequest);

            if (error.size() >= 1) {
                addToRequestFails(requestFails, requestFail, error);
                continue;
            }

            CompletableFuture<List<KeeperOrder>> keeperOrderListByDeviceId = _keeperOrderService.getListByDeviceId(submittedRequest.getDeviceId());

            if (keeperOrderListByDeviceId.get().size() == 3) {
                error.add("Keeper number exceeds the allowance of times");
            }

            String requestId = UUID.randomUUID().toString().replace("-", "");
            Request newRequest = new Request();
            newRequest.setRequestId(requestId);
            newRequest.setRequester_Id(requester.getId());
            newRequest.setNextKeeper_Id(nextKeeper.getId());
            newRequest.setBookingDate(submittedRequest.getBookingDate());
            newRequest.setReturnDate(submittedRequest.getReturnDate());
            newRequest.setDevice_Id(device.getId());
            newRequest.setCreatedDate(new Date());

            if (checkRequestWhenSubmitting(requestSuccessful, newRequest)) {
                error.add("There are more than 2 identical requests when submitting");
                addToRequestFails(requestFails, requestFail, error);
                continue;
            }

            if (checkWhenDevicesAreSimilar(requestSuccessful, newRequest)) {
                error.add("There are more than 2 identical devices when submitting");
                addToRequestFails(requestFails, requestFail, error);
                continue;
            }

            /* Order number = 1, the current keeper is the original owner */
            if (keeperOrderListByDeviceId.get().size() == 0) {

                if (isSubmittedRequestExistentInDatabase(requester.getId(), owner.getId(), nextKeeper.getId(), device.getId())) {
                    error.add("The submitted request is already existent in the database");
                }

                if (error.size() >= 1) {
                    addToRequestFails(requestFails, requestFail, error);
                    continue;
                }

                /* the OWNER of the booked device have to concur the NEXT KEEPER to keep it */
                addRequestToList(newRequest, requestSuccessful, owner.getId());
            }
            /*  Order number > 1, the current keeper is not the original owner */
            addRequestWhenOrderIsNotTheFirst(keeperOrderListByDeviceId.get(), submittedRequest, newRequest, requestFails, requestFail, error, requestSuccessful);
        }

        if (requestFails.size() > 0) {
            /* if there are fails */
            response.setFailedRequestsList(requestFails);
            return CompletableFuture.completedFuture(response);
        }

        for (Request request : requestSuccessful) _requestRepository.save(request);
        response.setFailedRequestsList(requestFails);
        return CompletableFuture.completedFuture(response);
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> showRequestListsWithPaging(int employeeId, int pageIndex, int pageSize, String sortBy, String sortDir, RequestFilterDTO requestFilter)
            throws InterruptedException, ExecutionException {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        List<Request> requests = _requestRepository.findAllRequest(employeeId, sort);
        requests = getAllRequests(employeeId, requestFilter, requests).get();
        List<String> requestStatusList = requests.stream().map(c -> RequestStatus.fromNumber(c.getRequestStatus()).get().toString()).distinct().collect(Collectors.toList());
        int totalElements = requests.size();
        requests = getPage(requests, pageIndex, pageSize).get();
        List<RequestDTO> requestList = requests.stream().map(request -> new RequestDTO(request, employeeId)).collect(Collectors.toList());
        ShowRequestsResponse response = new ShowRequestsResponse();
        response.setRequestsList(requestList);
        response.setPageNo(pageIndex);
        response.setPageSize(pageSize);
        response.setTotalElements(totalElements);
        response.setTotalPages(getTotalPages(pageSize, totalElements));
        response.setRequestStatusList(requestStatusList);
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, OK));
    }

    @Async
    @Override
    public CompletableFuture<KeywordSuggestionResponse> getSuggestKeywordRequests(int employeeId, int fieldColumn, String keyword, RequestFilterDTO requestFilter) throws InterruptedException, ExecutionException {
        Set<String> keywordList = new HashSet<>();
        Sort sort = Sort.by("Id").ascending();
        List<Request> requests = _requestRepository.findAllRequest(employeeId, sort);
        requests = getAllRequests(employeeId, requestFilter, requests).get();
        Stream<String> mappedDeviceList = null;

        switch (fieldColumn) {
            case REQUEST_REQUEST_ID_COLUMN -> mappedDeviceList = requests.stream().map(Request::getRequestId);
            case REQUEST_DEVICE_NAME_COLUMN -> mappedDeviceList = requests.stream().map(r -> r.getDevice().getName());
            case REQUEST_DEVICE_SERIAL_NUMBER_COLUMN ->
                    mappedDeviceList = requests.stream().map(r -> r.getDevice().getSerialNumber());
            case REQUEST_REQUESTER_COLUMN ->
                    mappedDeviceList = requests.stream().map(r -> r.getRequester().getUserName());
            case REQUEST_CURRENT_KEEPER_COLUMN ->
                    mappedDeviceList = requests.stream().map(r -> r.getCurrentKeeper().getUserName());
            case REQUEST_NEXT_KEEPER_COLUMN ->
                    mappedDeviceList = requests.stream().map(r -> r.getNextKeeper().getUserName());
            case REQUEST_APPROVER_COLUMN ->
                    mappedDeviceList = requests.stream().map(r -> r.getAccepter().getUserName());
        }

        if (mappedDeviceList != null) {
            keywordList = mappedDeviceList
                    .filter(element -> element.toLowerCase().contains(keyword.strip().toLowerCase()))
                    .limit(20)
                    .collect(Collectors.toSet());
        }

        KeywordSuggestionResponse response = new KeywordSuggestionResponse();
        response.setKeywordList(keywordList);
        return CompletableFuture.completedFuture(response);
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> updateRequestStatus(UpdateStatusRequestDTO requestDTO) throws InterruptedException, ExecutionException {
        Optional<Request> request = _requestRepository.findById((long) requestDTO.getRequestId());

        /* If the request in the database does not exist */
        if (request.isEmpty()) return CompletableFuture.completedFuture(new ResponseEntity<>(false, NOT_FOUND));
        
        /* If the SUBMITTED request's status has the same value to the request in the DATABASE and request status must NOT be EXTENDING
            EX: Status in database = APPROVED, Submitted status = APPROVED => wrong
         */
        boolean isRequestStatusInvalid = request.get().getRequestStatus() == requestDTO.getRequestStatus() && requestDTO.getRequestStatus() != EXTENDING;
        if (isRequestStatusInvalid)
            return CompletableFuture.completedFuture(new ResponseEntity<>(false, NOT_ACCEPTABLE));

        switch (requestDTO.getRequestStatus()) {
            case APPROVED -> {
                request.get().setApprovalDate(new Date());
                changeStatus(request.get(), APPROVED);
                cancelRelatedPendingRequest(request.get());
                changeDeviceStatus(request.get());
            }
            case CANCELLED -> {
                request.get().setApprovalDate(new Date());
                changeStatus(request.get(), CANCELLED);
            }
            case TRANSFERRED -> {
                request.get().setTransferredDate(new Date());
                changeStatus(request.get(), TRANSFERRED);
                List<KeeperOrder> keeperOrderList = _keeperOrderService.getListByDeviceId(request.get().getDevice().getId()).get();
                int keeperNo = returnKeeperNo(keeperOrderList);
                KeeperOrder keeperOrder = new KeeperOrder();
                keeperOrder.setDevice_Id(request.get().getDevice().getId());
                keeperOrder.setKeeper_Id(request.get().getNextKeeper().getId());
                keeperOrder.setKeeperNo(keeperNo + 1);  /* By virtue of being a new keeper order, keeperNo is increased */
                keeperOrder.setBookingDate(request.get().getBookingDate());
                keeperOrder.setDueDate(request.get().getReturnDate());
                keeperOrder.setIsReturned(false);
                keeperOrder.setCreatedDate(new Date());
                keeperOrder.setUpdatedDate(new Date());
                _keeperOrderService.create(keeperOrder);
            }
            case EXTENDING -> {
                /* The request will change its status and update approval date */
                /* Accept extending */
                request.get().setApprovalDate(new Date());
                changeStatus(request.get(), TRANSFERRED);
                changeOldRequest(request.get());
                /* UPDATE order's due date */
                KeeperOrder keeperOrder = _keeperOrderService.findByDeviceIdAndKeeperId(
                        request.get().getDevice().getId(),
                        request.get().getNextKeeper_Id()).get();
                keeperOrder.setDueDate(request.get().getReturnDate());
                keeperOrder.setUpdatedDate(new Date());
                cancelRelatedExtendingRequest(request.get());
                _keeperOrderService.update(keeperOrder);
            }
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(true, OK));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> extendDurationRequest(ExtendDurationRequestDTO request) throws InterruptedException, ExecutionException, ParseException {
        /*
            SHOW MAX EXTENDING RETURN DATE IN SHOWING ALL KEEPING DEVICE PAGE
            Implement this method to updateReturnStatus
         *  Find the current request via next keeper, device and status
         *  Find the previous order to have the max duration for the device
         *  Create a new request for sending requests to the person accepting reviews it
         * */
        CompletableFuture<User> nextKeeper = _employeeService.findByUsername(request.getNextKeeper());
        Device device = _deviceRepository.findById(request.getDeviceId());

        if (checkExtendDurationRequest(request, nextKeeper.get(), device) != null)
            return CompletableFuture.completedFuture(new ResponseEntity<>(checkExtendDurationRequest(request, nextKeeper.get(), device), NOT_FOUND));

        List<KeeperOrder> keeperOrderByDeviceIdList = _keeperOrderService.getListByDeviceId(device.getId()).get();
        KeeperOrder currentKeeperOrder = returnCurrentKeeperOrder(keeperOrderByDeviceIdList, nextKeeper.get()).get();

        /* There is no UNRETURNED keeper order pertaining to the provided device ID */
        if (invalidCurrentOrder(currentKeeperOrder))
            return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageResponse("Request is not approved"), NOT_FOUND));

        /*  No 1: 1/9 - 1/11
         *  No 2: 16/9 - 16/10
         *  return date of B cannot be before 16/10
         *  and cannot exceed 1/11
         * */
        if (isKeeperNoGreaterThan1(currentKeeperOrder)) {
            int currentOrderNumber = currentKeeperOrder.getKeeperNo();
            if (doesReturnDateExceedLimitation(request, keeperOrderByDeviceIdList, currentOrderNumber))
                return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageResponse("Return date exceeds the allowed duration!"), NOT_FOUND));
        }

        Request preExtendingDurationRequest = findAnOccupiedRequest(nextKeeper.get().getId(), device.getId()).get();
        Request postExtendingDurationRequest = new Request();
        postExtendingDurationRequest.setRequester_Id(preExtendingDurationRequest.getRequester_Id());
        postExtendingDurationRequest.setRequestId(preExtendingDurationRequest.getRequestId());
        postExtendingDurationRequest.setCurrentKeeper_Id(preExtendingDurationRequest.getCurrentKeeper_Id());
        postExtendingDurationRequest.setNextKeeper_Id(nextKeeper.get().getId());
        postExtendingDurationRequest.setBookingDate(preExtendingDurationRequest.getBookingDate());
        postExtendingDurationRequest.setReturnDate(request.getReturnDate());
        postExtendingDurationRequest.setDevice_Id(preExtendingDurationRequest.getDevice_Id());
        postExtendingDurationRequest.setCreatedDate(preExtendingDurationRequest.getCreatedDate());
        postExtendingDurationRequest.setUpdatedDate(new Date());
        postExtendingDurationRequest.setAccepter_Id(preExtendingDurationRequest.getAccepter_Id());
        postExtendingDurationRequest.setTransferredDate(preExtendingDurationRequest.getTransferredDate());
        postExtendingDurationRequest.setRequestStatus(EXTENDING);
        _requestRepository.save(postExtendingDurationRequest);
        return CompletableFuture.completedFuture(new ResponseEntity<>(new MessageResponse("Send request successfully"), OK));
    }

    @Async
    @Override
    public CompletableFuture<Request> findAnOccupiedRequest(int nextKeeperId, int deviceId) {
        return CompletableFuture.completedFuture(_requestRepository.findAnOccupiedRequest(nextKeeperId, deviceId));
    }

    @Override
    public void updateRequest(Request request) {
        _requestRepository.save(request);
    }

    @Override
    public boolean findRequestBasedOnStatusAndDevice(int deviceId, int requestStatus) {
        List<Request> requests = _requestRepository.findRequestBasedOnStatusAndDevice(deviceId, requestStatus);
        return requests.size() != 0;
    }

    @Override
    public void deleteRequestBasedOnStatusAndDevice(int deviceId, int requestStatus) {
        if (findRequestBasedOnStatusAndDevice(deviceId, requestStatus)) {
            List<Request> requests = _requestRepository.findRequestBasedOnStatusAndDevice(deviceId, requestStatus);
            for (Request request : requests) {
                _requestRepository.delete(request);
            }
        }
    }

    private void addToRequestFails(List<RequestFails> requestFails, RequestFails requestFail, List<String> error) {
        requestFail.setErrorMessage(error);
        requestFails.add(requestFail);
    }

    @Async
    private CompletableFuture<List<Request>> getPage(List<Request> sourceList, int pageIndex, int pageSize) {
        if (pageSize <= 0 || pageIndex <= 0)
            throw new IllegalArgumentException("invalid page size: " + pageSize);

        int fromIndex = (pageIndex - 1) * pageSize;
        if (sourceList == null || sourceList.size() <= fromIndex)
            return CompletableFuture.completedFuture(Collections.emptyList());

        return CompletableFuture.completedFuture(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));
    }

    @Async
    private CompletableFuture<List<Request>> fetchFilteredRequest(RequestFilterDTO requestFilter, List<Request> requests) {
        if (requestFilter.getRequestId() != null)
            requests = requests.stream().filter(request -> request.getRequestId().equals(requestFilter.getRequestId())).collect(Collectors.toList());

        if (requestFilter.getDevice() != null)
            requests = requests.stream().filter(request -> request.getDevice().getName().toLowerCase().equals(requestFilter.getDevice())).collect(Collectors.toList());

        if (requestFilter.getSerialNumber() != null)
            requests = requests.stream().filter(request -> request.getDevice().getSerialNumber().toLowerCase().equals(requestFilter.getSerialNumber())).collect(Collectors.toList());

        if (requestFilter.getApprover() != null)
            requests = requests.stream().filter(request -> request.getAccepter().getUserName().toLowerCase().equals(requestFilter.getApprover())).collect(Collectors.toList());

        if (requestFilter.getRequester() != null)
            requests = requests.stream().filter(request -> request.getRequester().getUserName().toLowerCase().equals(requestFilter.getRequester())).collect(Collectors.toList());

        if (requestFilter.getCurrentKeeper() != null)
            requests = requests.stream().filter(request -> request.getCurrentKeeper().getUserName().toLowerCase().equals(requestFilter.getCurrentKeeper())).collect(Collectors.toList());

        if (requestFilter.getNextKeeper() != null)
            requests = requests.stream().filter(request -> request.getNextKeeper().getUserName().toLowerCase().equals(requestFilter.getNextKeeper())).collect(Collectors.toList());

        if (requestFilter.getRequestStatus() != null)
            requests = requests.stream().filter(request -> request.getRequestStatus() == RequestStatus.valueOf(requestFilter.getRequestStatus()).ordinal()).collect(Collectors.toList());

        if (requestFilter.getBookingDate() != null)
            requests = requests.stream().filter(device -> device.getBookingDate() != null).filter(request -> request.getBookingDate().after(requestFilter.getBookingDate())).collect(Collectors.toList());

        if (requestFilter.getReturnDate() != null)
            requests = requests.stream().filter(device -> device.getReturnDate() != null).filter(request -> request.getReturnDate().before(requestFilter.getReturnDate())).collect(Collectors.toList());

        return CompletableFuture.completedFuture(requests);
    }

    private int getTotalPages(int pageSize, int listSize) {
        if (listSize == 0)
            return 1;

        if (listSize % pageSize == 0)
            return listSize / pageSize;

        return (listSize / pageSize) + 1;
    }

    private void formatFilter(RequestFilterDTO requestFilter) {
        if (requestFilter.getRequester() != null)
            requestFilter.setRequester(requestFilter.getRequester().trim().toLowerCase());

        if (requestFilter.getApprover() != null)
            requestFilter.setApprover(requestFilter.getApprover().trim().toLowerCase());

        if (requestFilter.getCurrentKeeper() != null)
            requestFilter.setCurrentKeeper(requestFilter.getCurrentKeeper().trim().toLowerCase());

        if (requestFilter.getNextKeeper() != null)
            requestFilter.setNextKeeper(requestFilter.getNextKeeper().trim().toLowerCase());

        if (requestFilter.getDevice() != null)
            requestFilter.setDevice(requestFilter.getDevice().trim().toLowerCase());
    }

    @Async
    private CompletableFuture<List<Request>> getAllRequests(int employeeId, RequestFilterDTO requestFilter, List<Request> requests) throws ExecutionException, InterruptedException {
        formatFilter(requestFilter);
        requests = fetchFilteredRequest(requestFilter, requests).get();
        requests = requests.stream()
                .filter(request -> isExtendingRequestViewableForNonAcceptorRequester(request, employeeId))
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(requests);
    }

    private boolean isExtendingRequestViewableForNonAcceptorRequester(Request request, int employeeId) {
        return !(request.getRequestStatus() == EXTENDING && employeeId == request.getRequester().getId() && employeeId != request.getAccepter().getId());
    }

    private boolean isRequestListInvalid(List<Request> requestList) {
        return requestList != null;
    }

    /* The old transferred request's status will be changed to CANCELLED for EXTENDING CASE */
    private void changeOldRequest(Request request) {
        List<Request> preExtendDurationRequest = _requestRepository.findDeviceRelatedApprovedRequest(request.getId(), request.getCurrentKeeper_Id(), request.getDevice().getId(), TRANSFERRED);
        if (isRequestListInvalid(preExtendDurationRequest)) {
            for (Request relatedRequest : preExtendDurationRequest) {
                relatedRequest.setRequestStatus(CANCELLED);
                relatedRequest.setCancelledDate(new Date());
                _requestRepository.save(relatedRequest);
            }
        }
    }

    /* Change device status to OCCUPIED when a request is approved */
    private void changeDeviceStatus(Request request) throws ExecutionException, InterruptedException {
        Device device = _deviceRepository.findById(request.getDevice().getId());
        if (device.getStatus() != Status.OCCUPIED) {
            device.setStatus(Status.OCCUPIED);
            _deviceRepository.save(device);
        }
    }

    /* Get the latest keeper order's number */
    private int returnKeeperNo(List<KeeperOrder> keeperOrderList) {
        return keeperOrderList.size() > 0 ? keeperOrderList.stream().max(Comparator.comparing(KeeperOrder::getKeeperNo)).map(KeeperOrder::getKeeperNo).get() : 0;
    }

    /* Cancel all related pending requests except the SUBMITTED request */
    private void cancelRelatedPendingRequest(Request request) {
        List<Request> relatedRequests = _requestRepository.findDeviceRelatedApprovedRequest(request.getId(), request.getCurrentKeeper_Id(), request.getDevice().getId(), PENDING);
        if (isRequestListInvalid(relatedRequests)) {
            for (Request relatedRequest : relatedRequests) {
                relatedRequest.setRequestStatus(CANCELLED);
                relatedRequest.setCancelledDate(new Date());
                _requestRepository.save(relatedRequest);
            }
        }
    }

    /* Cancel all related extending requests except the SUBMITTED request */
    private void cancelRelatedExtendingRequest(Request request) {
        List<Request> relatedRequests = _requestRepository.findDeviceRelatedApprovedRequest(request.getId(), request.getCurrentKeeper_Id(), request.getDevice().getId(), EXTENDING);
        if (isRequestListInvalid(relatedRequests)) {
            for (Request relatedRequest : relatedRequests) {
                relatedRequest.setRequestStatus(CANCELLED);
                relatedRequest.setCancelledDate(new Date());
                _requestRepository.save(relatedRequest);
            }
        }
    }

    @Async
    private CompletableFuture<KeeperOrder> returnCurrentKeeperOrder(List<KeeperOrder> keeperOrderList, User nextKeeper) {
        KeeperOrder currentKeeperOrder = new KeeperOrder();
        for (KeeperOrder k : keeperOrderList) {
            if (k.getKeeper().getId() == nextKeeper.getId())
                currentKeeperOrder = k;
        }
        return CompletableFuture.completedFuture(currentKeeperOrder);
    }

    private String checkExtendDurationRequest(ExtendDurationRequestDTO request, User nextKeeper, Device device) throws ExecutionException, InterruptedException {
        if (request.getReturnDate() == null)
            return "Return date must not be empty!";

        if (isUserInvalid(nextKeeper))
            return "Next keeper is not existent";

        if (isDeviceInvalid(device))
            return "Device is not existent";

        Request currentRequest = findAnOccupiedRequest(nextKeeper.getId(), device.getId()).get();
        if (isRequestInvalid(currentRequest))
            return "Request is not existent";
        /*  A: 1/9 - 1/11
         *  B: 16/9 - 16/10
         *  return date of B cannot be before 16/10
         * */
        if (isReturnDateBeforeCurrentRequest(request, currentRequest))
            return "Return date must be after than the available current return date!";
        return null;
    }

    private boolean isUserInvalid(User user) {
        return user == null;
    }

    private boolean isDeviceInvalid(Device device) {
        return device == null;
    }

    private boolean isRequestInvalid(Request request) {
        return request == null;
    }

    private boolean isReturnDateBeforeCurrentRequest(ExtendDurationRequestDTO request, Request currentRequest) {
        return request.getReturnDate().before(currentRequest.getReturnDate());
    }

    private boolean invalidCurrentOrder(KeeperOrder currentKeeperOrder) {
        return currentKeeperOrder == null;
    }

    private boolean isKeeperNoGreaterThan1(KeeperOrder currentKeeperOrder) {
        return currentKeeperOrder.getKeeperNo() > 1;
    }

    private boolean doesReturnDateExceedLimitation(ExtendDurationRequestDTO newRequest, List<KeeperOrder> keeperOrderList, int currentOrderNumber) {
        KeeperOrder previousKeeperOrder = keeperOrderList.stream().filter(k -> k.getKeeperNo() == currentOrderNumber - 1).findFirst().get();
        return newRequest.getReturnDate().after(previousKeeperOrder.getDueDate());
    }

    private void changeStatus(Request request, int requestStatus) {
        request.setRequestStatus(requestStatus);
        _requestRepository.save(request);
    }

    private void setUpRequestFail(RequestFails requestFail, SubmitBookingRequestDTO.RequestInput request, Device device) {
        requestFail.setDeviceId(request.getDeviceId());
        requestFail.setRequester(request.getRequester().trim());
        requestFail.setNextKeeper(request.getNextKeeper().trim());
        requestFail.setBookingDate(request.getBookingDate());
        requestFail.setReturnDate(request.getReturnDate());
        requestFail.setDeviceName(device.getName().trim());
    }

    private boolean isSubmittedRequestExistentInDatabase(int requesterId, int ownerId, int nextKeeperId, int deviceId) {
        return _requestRepository.findRepetitiveRequest(requesterId, ownerId, nextKeeperId, deviceId) != null;
    }

    private boolean areSubmittedRequestIdentical(Request oldRequest, Request newRequest) {
        return oldRequest.getDevice_Id() == newRequest.getDevice_Id() && oldRequest.getRequester_Id() == newRequest.getRequester_Id() && oldRequest.getNextKeeper_Id() == newRequest.getNextKeeper_Id();
    }

    private boolean areDeviceIdenticalWhenSubmitting(Request oldRequest, Request newRequest) {
        return oldRequest.getDevice_Id() == newRequest.getDevice_Id();
    }

    private boolean checkRequestWhenSubmitting(List<Request> requestSuccessful, Request newRequest) {
        for (Request oldRequest : requestSuccessful) {
            if (areSubmittedRequestIdentical(oldRequest, newRequest))
                return true;
        }
        return false;
    }

    private boolean checkWhenDevicesAreSimilar(List<Request> requestSuccessful, Request newRequest) {
        for (Request oldRequest : requestSuccessful) {
            if (areDeviceIdenticalWhenSubmitting(oldRequest, newRequest))
                return true;
        }
        return false;
    }

    private void addRequestToList(Request requestData, List<Request> requestSuccessful, int userId) {
        requestData.setAccepter_Id(userId);
        requestData.setCurrentKeeper_Id(userId);
        requestData.setRequestStatus(PENDING);
        requestSuccessful.add(requestData);
    }

    private void checkAnEmptyList(SubmitBookingRequestDTO requests, List<RequestFails> requestFails) {
        if (requests.getRequestsList().size() == 0) {
            RequestFails requestFail = new RequestFails();
            List<String> error = new ArrayList<>();
            error.add("You didn't submit requests");
            requestFail.setErrorMessage(error);
            requestFails.add(requestFail);
        }
    }

    private void validateRequestInput(List<String> error, Device device, User requester, User nextKeeper, SubmitBookingRequestDTO.RequestInput request) {
        boolean isDeviceUsable = !device.getStatus().name().equalsIgnoreCase("broken") && !device.getStatus().name().equalsIgnoreCase("unavailable"),
                isNextKeeperValid = nextKeeper != null && !request.getNextKeeper().trim().equalsIgnoreCase(device.getOwner().getUserName()),
                areDatesInvalid = (request.getBookingDate() == null || request.getBookingDate().toString().isEmpty())
                        || (request.getReturnDate() == null || request.getReturnDate().toString().isEmpty()),
                isRequesterInvalid = requester == null;

        if (!isDeviceUsable) {
            error.add("The device you submitted is unusable");
        }
        if (isRequesterInvalid) {
            error.add("The requester is invalid");
        }
        if (!isNextKeeperValid) {
            error.add("The next keeper you submitted must be non-null or not identical to a device's owner");
        }
        if (areDatesInvalid) {
            error.add("The dates you submitted must be non-null");
        } else {
            boolean isBookingGreaterThanReturnDate = request.getBookingDate().before(request.getReturnDate());
            if (!isBookingGreaterThanReturnDate)
                error.add("The booking date must be less than return date");
        }
    }

    private void addRequestWhenOrderIsNotTheFirst(List<KeeperOrder> keeperOrderListByDeviceId, SubmitBookingRequestDTO.RequestInput submittedRequestDTO, Request requestData,
                                                  List<RequestFails> requestFails, RequestFails requestFail, List<String> error, List<Request> requestSuccessful) {
        for (KeeperOrder keeperOrder : keeperOrderListByDeviceId) {
            boolean areDatesInDuration = submittedRequestDTO.getBookingDate().before(submittedRequestDTO.getReturnDate()) &&
                    submittedRequestDTO.getBookingDate().after(keeperOrder.getBookingDate()) &&
                    submittedRequestDTO.getReturnDate().before(keeperOrder.getDueDate());

            if (requestData.getNextKeeper_Id() == keeperOrder.getKeeper().getId()) {
                /* B borrowed A's
                   C borrowed B's
                   A and B cannot borrow C's
                */
                error.add("The next keeper is existent in keeper order. Please try another next keeper");
            }

            if (!areDatesInDuration) {
                /*  1: B borrowed A's from 1/7 - 1/10
                 *  2: C borrowed B's from 1/8 - 1/9
                 *  3: D borrowed C's from 2/8 - 15/8
                 *  Hence, Booking date and return date must be in the valid date range of the latest keeper order.
                 *  Ex: 3's request must be from 2/8 - 31/8
                 */
                error.add("The booking date and/or return date are out of keeper order's date range");
            }

            if (isSubmittedRequestExistentInDatabase(requestData.getRequester_Id(), keeperOrder.getKeeper().getId(), requestData.getNextKeeper_Id(), requestData.getDevice_Id())) {
                error.add("The submitted request is already existent in the database");
            }

            if (error.size() >= 1) {
                addToRequestFails(requestFails, requestFail, error);
                break;
            }
            /* other keepers keeping the booked device have to concur the NEXT KEEPER to keep it */
            addRequestToList(requestData, requestSuccessful, keeperOrder.getKeeper().getId());
        }
    }
}
