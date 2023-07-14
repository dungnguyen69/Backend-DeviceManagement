package com.fullstack.Backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.Backend.entities.Request;
import com.fullstack.Backend.enums.RequestStatus;

import java.util.Date;

import static com.fullstack.Backend.constant.constant.EXTENDING;

public class RequestDTO {
    @JsonProperty("Id")
    public int Id;
    @JsonProperty("request_id")
    public String request_id;
    @JsonProperty("current_keeper")
    public String current_keeper;
    @JsonProperty("next_keeper")
    public String next_keeper;
    @JsonProperty("requester")
    public String requester;
    @JsonProperty("request_status")
    public String request_status;
    @JsonProperty("device_name")
    public String device_name;

    @JsonProperty("device_serial_number")
    public String device_serial_number;

    @JsonProperty("device_id")
    public int device_id;
    @JsonProperty("accepter")
    public String accepter;
    @JsonProperty("BookingDate")
    public Date BookingDate;
        @JsonProperty("ReturnDate")
    public Date ReturnDate;
    @JsonProperty("created_date")
    public Date created_date;
    @JsonProperty("updated_date")
    public Date updated_date;
    @JsonProperty("approval_date")
    public Date approval_date;
    @JsonProperty("transferred_date")
    public Date transferred_date;
    @JsonProperty("cancelled_date")
    public Date cancelled_date;

    public RequestDTO(Request request, int employeeId) {
        this.Id = request.getId();
        this.request_id = request.getRequestId();
        this.requester = request.getRequester().getUserName();
        this.current_keeper = request.getCurrentKeeper().getUserName();
        this.next_keeper = request.getNextKeeper().getUserName();
        this.accepter = request.getAccepter().getUserName();
        if(request.getRequestStatus() == EXTENDING && employeeId == request.getRequester().getId() && employeeId != request.getAccepter().getId()){
            this.request_status = RequestStatus.fromNumber(request.getRequestStatus()).get().toString();
        }
        this.request_status = RequestStatus.fromNumber(request.getRequestStatus()).get().toString();
        this.device_name = request.getDevice().getName();
        this.device_id = request.getDevice().getId();
        this.device_serial_number = request.getDevice().getSerialNumber();
        this.BookingDate = request.getBookingDate();
        this.ReturnDate = request.getReturnDate();
        this.created_date = request.getCreatedDate();
        this.updated_date = request.getUpdatedDate();
        this.approval_date = request.getApprovalDate();
        this.transferred_date = request.getTransferredDate();
        this.cancelled_date = request.getCancelledDate();
    }
}
