package com.fullstack.Backend.services.impl;import com.fullstack.Backend.dto.request.RequestDTO;import com.fullstack.Backend.dto.request.RequestFilterDTO;import com.fullstack.Backend.dto.request.SubmitBookingRequestDTO;import com.fullstack.Backend.dto.request.UpdateStatusRequestDTO;import com.fullstack.Backend.entities.Device;import com.fullstack.Backend.entities.KeeperOrder;import com.fullstack.Backend.entities.Request;import com.fullstack.Backend.entities.User;import com.fullstack.Backend.enums.RequestStatus;import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;import com.fullstack.Backend.repositories.interfaces.IRequestRepository;import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;import com.fullstack.Backend.responses.request.ShowRequestsResponse;import com.fullstack.Backend.responses.request.SubmitBookingResponse;import com.fullstack.Backend.services.*;import com.fullstack.Backend.utils.RequestFails;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.*;import java.util.concurrent.CompletableFuture;import java.util.concurrent.ExecutionException;import java.util.stream.Collectors;import static com.fullstack.Backend.constant.constant.*;@Servicepublic class RequestService implements IRequestService {    @Autowired    IRequestRepository _requestRepository;    @Autowired    IEmployeeService _employeeService;    @Autowired    IKeeperOrderService _keeperOrderService;    @Autowired    IDeviceRepository _deviceRepository;    @Override    public CompletableFuture<SubmitBookingResponse> submitBookingRequest(SubmitBookingRequestDTO requests) throws InterruptedException, ExecutionException {        List<RequestFails> requestFails = new ArrayList<RequestFails>();        List<Request> requestSuccessful = new ArrayList<Request>();        SubmitBookingResponse response = new SubmitBookingResponse();        for (var request : requests.getRequestsList()) {            RequestFails requestFail = new RequestFails();            requestFail.setRequester(request.getRequester().trim());            requestFail.setCurrentKeeper(request.getCurrentKeeper().trim());            requestFail.setNextKeeper(request.getNextKeeper().trim());            requestFail.setBookingDate(request.getBookingDate());            requestFail.setReturnDate(request.getReturnDate());            User requester = _employeeService.findByUsername(request.getRequester().trim()).get(),                    currentKeeper = _employeeService.findByUsername(request.getCurrentKeeper().trim()).get(),                    nextKeeper = _employeeService.findByUsername(request.getNextKeeper().trim()).get();            if (!_deviceRepository.existsById((long) request.getDeviceId())) {                requestFail.setErrorMessage("The device you submitted is not existed");                requestFails.add(requestFail);                continue;            }            Device device = _deviceRepository.findById(request.getDeviceId());            requestFail.setDeviceName(device.getName().trim());            requestFail.setPlatformName(device.getPlatform().getName().trim());            requestFail.setPlatformVersion(device.getPlatform().getVersion().trim());            requestFail.setItemType(device.getItemType().getName().trim());            requestFail.setRamSize(device.getRam().getSize().toString().trim());            requestFail.setStorageSize(device.getStorage().getSize().toString().trim());            requestFail.setScreenSize(device.getScreen().getSize().toString().trim());            requestFail.setInventoryNumber(device.getInventoryNumber().trim());            requestFail.setSerialNumber(device.getSerialNumber().trim());            Boolean isDeviceUsable = !device.getStatus().name().toString().equalsIgnoreCase("broken") && !device.getStatus().name().toString().equalsIgnoreCase("unavailable"),                    isNextKeeperValid = nextKeeper != currentKeeper && nextKeeper != null && !request.getNextKeeper().trim().equalsIgnoreCase(device.getOwner().getUserName()),                    isDateValid = request.getBookingDate() != null && request.getReturnDate() != null;            if (!isDeviceUsable) {                requestFail.setErrorMessage("The device you submitted is unusable");                requestFails.add(requestFail);                continue;            }            if (!isNextKeeperValid) {                requestFail.setErrorMessage("The next keeper you submitted is invalid");                requestFails.add(requestFail);                continue;            }            if (!isDateValid) {                requestFail.setErrorMessage("The dates you submitted are invalid");                requestFails.add(requestFail);                continue;            }            String requestId = UUID.randomUUID().toString().replace("-", "");            Request requestData = new Request();            requestData.setRequestId(requestId);            requestData.setRequester_Id(requester.getId());            requestData.setCurrentKeeper_Id(currentKeeper.getId());            requestData.setNextKeeper_Id(nextKeeper.getId());            requestData.setBookingDate(request.getBookingDate());            requestData.setReturnDate(request.getReturnDate());            requestData.setDevice_Id(device.getId());            requestData.setCreatedDate(new Date());            CompletableFuture<List<KeeperOrder>> keeperOrderList = _keeperOrderService.getKeeperOrderListByDeviceId(request.getDeviceId());            boolean isDeviceBooked = false;            for (KeeperOrder keeperOrder : keeperOrderList.get()) {                boolean areBookingDateAndDueDateValid = request.getBookingDate().before(request.getReturnDate()) && request.getBookingDate().after(keeperOrder.getBookingDate()) && request.getReturnDate().before(keeperOrder.getDueDate());                if (areBookingDateAndDueDateValid) {                    if (nextKeeper.getId() == keeperOrder.getKeeper().getId()) {                        requestFail.setErrorMessage("The next keeper invalid");                        requestFails.add(requestFail);                        isDeviceBooked = true;                        break;                    }                    requestData.setAccepter_Id(keeperOrder.getKeeper().getId());                    requestData.setRequestStatus(RequestStatus.values()[PENDING]);                    isDeviceBooked = true;                    requestSuccessful.add(requestData);                    break;                }            }            if (!isDeviceBooked) {                User owner = _employeeService.findByUsername(device.getOwner().getUserName()).get();                requestData.setAccepter_Id(owner.getId());                requestData.setRequestStatus(RequestStatus.values()[PENDING]);                requestSuccessful.add(requestData);            }        }        if (requestFails.size() > 0) {            response.setFailedRequestsList(requestFails);            return CompletableFuture.completedFuture(response);        }        for (Request request : requestSuccessful)            _requestRepository.save(request);        response.setFailedRequestsList(requestFails);        return CompletableFuture.completedFuture(response);    }    @Override    public CompletableFuture<ShowRequestsResponse> showRequestListsWithPaging(int employeeId, int pageIndex, int pageSize, String sortBy, String sortDir, RequestFilterDTO requestFilter)            throws InterruptedException, ExecutionException {        formatFilter(requestFilter);        List<Request> requests = _requestRepository.findAllRequest(employeeId, sortBy, sortDir);        List<String> requestStatusList = requests.stream().map(c -> c.getRequestStatus().name()).distinct().collect(Collectors.toList());        requests = fetchFilteredRequest(requestFilter, requests).get();        requests = getPage(requests, pageIndex, pageSize).get();        List<RequestDTO> requestList = requests.stream().map(request -> new RequestDTO(request)).collect(Collectors.toList());        ShowRequestsResponse response = new ShowRequestsResponse();        response.setRequestsList(requestList);        response.setPageNo(pageIndex);        response.setPageSize(pageSize);        response.setTotalElements(requests.size());        response.setTotalPages(getTotalPages(pageSize, requests.size()));        response.setRequestStatusList(requestStatusList);        return CompletableFuture.completedFuture(response);    }    @Override    public int getTotalPages(int pageSize, int listSize) {        if (listSize == 0)            return 1;        if (listSize % pageSize == 0)            return listSize / pageSize;        return (listSize / pageSize) + 1;    }    @Override    public void formatFilter(RequestFilterDTO requestFilter) {        if (requestFilter.getRequester() != null)            requestFilter.setRequester(requestFilter.getRequester().trim().toLowerCase());        if (requestFilter.getCurrentKeeper() != null)            requestFilter.setCurrentKeeper(requestFilter.getCurrentKeeper().trim().toLowerCase());        if (requestFilter.getNextKeeper() != null)            requestFilter.setNextKeeper(requestFilter.getNextKeeper().trim().toLowerCase());        if (requestFilter.getDevice() != null)            requestFilter.setDevice(requestFilter.getDevice().trim().toLowerCase());    }    @Override    public CompletableFuture<List<Request>> getPage(List<Request> sourceList, int pageIndex, int pageSize) {        if (pageSize <= 0 || pageIndex <= 0)            throw new IllegalArgumentException("invalid page size: " + pageSize);        int fromIndex = (pageIndex - 1) * pageSize;        if (sourceList == null || sourceList.size() <= fromIndex)            return CompletableFuture.completedFuture(Collections.emptyList());        return CompletableFuture.completedFuture(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));    }    @Override    public CompletableFuture<List<Request>> fetchFilteredRequest(RequestFilterDTO requestFilter, List<Request> requests) {        if (requestFilter.getRequestId() != null)            requests = requests.stream().filter(request -> request.getRequestId().equals(requestFilter.getRequestId())).collect(Collectors.toList());        if (requestFilter.getDevice() != null)            requests = requests.stream().filter(request -> request.getDevice().getName().toLowerCase().equals(requestFilter.getDevice())).collect(Collectors.toList());        if (requestFilter.getRequester() != null)            requests = requests.stream().filter(request -> request.getRequester().getUserName().toLowerCase().equals(requestFilter.getRequester())).collect(Collectors.toList());        if (requestFilter.getCurrentKeeper() != null)            requests = requests.stream().filter(request -> request.getCurrentKeeper().getUserName().toLowerCase().equals(requestFilter.getCurrentKeeper())).collect(Collectors.toList());        if (requestFilter.getNextKeeper() != null)            requests = requests.stream().filter(request -> request.getNextKeeper().getUserName().toLowerCase().equals(requestFilter.getNextKeeper())).collect(Collectors.toList());        if (requestFilter.getRequestStatus() != null)            requests = requests.stream().filter(request -> request.getRequestStatus().name().equalsIgnoreCase(requestFilter.getRequestStatus())).collect(Collectors.toList());        if (requestFilter.getBookingDate() != null)            requests = requests.stream().filter(request -> request.getBookingDate().after(requestFilter.getBookingDate())).collect(Collectors.toList());        if (requestFilter.getReturnDate() != null)            requests = requests.stream().filter(request -> request.getReturnDate().before(requestFilter.getReturnDate())).collect(Collectors.toList());        return CompletableFuture.completedFuture(requests);    }    @Override    public CompletableFuture<KeywordSuggestionResponse> getSuggestKeywordRequests(int employeeId, int fieldColumn, String keyword, RequestFilterDTO requestFilter) throws InterruptedException, ExecutionException {        Set<String> keywordList = new HashSet<>();        List<Request> requests = _requestRepository.findAllRequest(employeeId, "Id", "ASC");        formatFilter(requestFilter);        requests = fetchFilteredRequest(requestFilter, requests).get();        switch (fieldColumn) {            case REQUEST_REQUEST_ID_COLUMN:                keywordList = requests.stream().filter(request -> request.getRequestId().contains(keyword)).map(r -> r.getRequestId()).collect(Collectors.toSet());                break;            case REQUEST_DEVICE_NAME_COLUMN:                keywordList = requests.stream().filter(request -> request.getDevice().getName().contains(keyword)).map(r -> r.getDevice().getName()).collect(Collectors.toSet());                break;            case REQUEST_REQUESTER_COLUMN:                keywordList = requests.stream().filter(request -> request.getRequester().getUserName().contains(keyword)).map(r -> r.getRequester().getUserName()).collect(Collectors.toSet());                break;            case REQUEST_CURRENT_KEEPER_COLUMN:                keywordList = requests.stream().filter(request -> request.getCurrentKeeper().getUserName().contains(keyword)).map(r -> r.getCurrentKeeper().getUserName()).collect(Collectors.toSet());                break;            case REQUEST_NEXT_KEEPER_COLUMN:                keywordList = requests.stream().filter(request -> request.getNextKeeper().getUserName().contains(keyword)).map(r -> r.getNextKeeper().getUserName()).collect(Collectors.toSet());                break;        }        KeywordSuggestionResponse response = new KeywordSuggestionResponse();        response.setKeywordList(keywordList);        return CompletableFuture.completedFuture(response);    }    @Override    public CompletableFuture<Boolean> updateRequestStatus(UpdateStatusRequestDTO requestDTO) throws InterruptedException, ExecutionException {        Request request = _requestRepository.findById(Long.valueOf(requestDTO.getRequestId())).get();        if (request == null) return CompletableFuture.completedFuture(false);        switch (requestDTO.getRequestStatus()) {            case APPROVED:                request.setRequestStatus(RequestStatus.APPROVED);                request.setApprovalDate(new Date());                _requestRepository.save(request);                List<Request> relatedRequests = _requestRepository.findRequestRelatedDeviceApproved(request.getId(), request.getCurrentKeeper_Id(), request.getDevice().getId(), PENDING);                if (relatedRequests != null) {                    for (Request relatedRequest : relatedRequests) {                        relatedRequest.setRequestStatus(RequestStatus.CANCELLED);                        relatedRequest.setCancelledDate(new Date());                        _requestRepository.save(relatedRequest);                    }                }                break;            case REJECTED:                request.setRequestStatus(RequestStatus.REJECTED);                request.setApprovalDate(new Date());                _requestRepository.save(request);                break;            case TRANSFERRED:                request.setRequestStatus(RequestStatus.TRANSFERRED);                request.setTransferredDate(new Date());                _requestRepository.save(request);                List<KeeperOrder> keeperOrderList = _keeperOrderService.getKeeperOrderListByDeviceId(request.getDevice().getId()).get();                int keeperNo = 0;                KeeperOrder keeperOrder = new KeeperOrder();                if (keeperOrderList.size() != 0)                    keeperNo = keeperOrderList.stream().max(Comparator.comparing(k -> k.getKeeperNo())).map(k -> k.getKeeperNo()).get();                keeperOrder.setDevice(request.getDevice());                keeperOrder.setKeeper(request.getNextKeeper());                keeperOrder.setKeeperNo(keeperNo);                keeperOrder.setBookingDate(request.getBookingDate());                keeperOrder.setDueDate(request.getReturnDate());                keeperOrder.setIsReturned(false);                keeperOrder.setCreatedDate(new Date());                keeperOrder.setUpdatedDate(new Date());                _keeperOrderService.createKeeperOrder(keeperOrder);                break;        }        return CompletableFuture.completedFuture(true);    }}