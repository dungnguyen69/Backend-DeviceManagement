package com.fullstack.Backend.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fullstack.Backend.dto.device.*;
import com.fullstack.Backend.dto.request.ReturnKeepDeviceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.responses.device.DropdownValuesResponse;
import com.fullstack.Backend.utils.dropdowns.OriginList;
import com.fullstack.Backend.utils.dropdowns.ProjectList;
import com.fullstack.Backend.utils.dropdowns.StatusList;

import jakarta.servlet.http.HttpServletResponse;

public interface IDeviceService {
    public CompletableFuture<ResponseEntity<Object>> showDevicesWithPaging(int pageIndex, int pageSize,
                                                                           String sortBy, String sortDir, FilterDeviceDTO deviceFilterDTO)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> addDevice(AddDeviceDTO device) throws ExecutionException, InterruptedException;

    public CompletableFuture<ResponseEntity<Object>> getDetailDevice(int deviceId)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> updateDevice(int deviceId, UpdateDeviceDTO device) throws ExecutionException, InterruptedException;

    public void formatFilter(FilterDeviceDTO deviceFilterDTO);

    public CompletableFuture<ResponseEntity<Object>> deleteDevice(int deviceId);

    public void exportToExcel(HttpServletResponse response) throws IOException, ExecutionException, InterruptedException;

    public void downloadTemplate(HttpServletResponse response)
            throws IOException, InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> importToDb(MultipartFile file) throws Exception;

    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordDevices(int fieldColumn, String keyword,
                                                                              FilterDeviceDTO deviceFilter) throws InterruptedException, ExecutionException;

    public int getTotalPages(int pageSize, int listSize);

    public CompletableFuture<List<Device>> fetchFilteredDevice(FilterDeviceDTO deviceFilter, List<Device> devices);

    public CompletableFuture<List<DeviceDTO>> getPage(List<DeviceDTO> sourceList, int pageIndex, int pageSize);

    public CompletableFuture<DropdownValuesResponse> getDropDownValues()
            throws InterruptedException, ExecutionException;

    public CompletableFuture<List<StatusList>> getStatusList();

    public CompletableFuture<List<ProjectList>> getProjectList();

    public CompletableFuture<List<OriginList>> getOriginList();

    public CompletableFuture<List<DeviceDTO>> getDevicesOfOwner(int ownerId, FilterDeviceDTO deviceFilter, String sortBy, String sortDir) throws ExecutionException, InterruptedException;

    public CompletableFuture<List<KeepingDeviceDTO>> getDevicesOfKeeper(int keeperId, FilterDeviceDTO deviceFilter) throws ExecutionException, InterruptedException;

    public CompletableFuture<List<DeviceDTO>> applyFilterBookingAndReturnDateForDevices(FilterDeviceDTO deviceFilter, List<DeviceDTO> devices);

    public CompletableFuture<ResponseEntity<Object>> updateReturnKeepDevice(ReturnKeepDeviceDTO request) throws ExecutionException, InterruptedException, ParseException;

    public CompletableFuture<ResponseEntity<Object>> updateReturnOwnedDevice(ReturnKeepDeviceDTO request) throws ExecutionException, InterruptedException, ParseException;

    public CompletableFuture<ResponseEntity<Object>> showOwnedDevicesWithPaging(int ownerId, int pageIndex, int pageSize, String sortBy, String sortDir, FilterDeviceDTO deviceFilter) throws ExecutionException, InterruptedException;

    public CompletableFuture<ResponseEntity<Object>> showKeepingDevicesWithPaging(int keeperId, int pageIndex, int pageSize, FilterDeviceDTO deviceFilter) throws ExecutionException, InterruptedException;

    public CompletableFuture<List<KeepingDeviceDTO>> fetchFilteredKeepingDevice(FilterDeviceDTO deviceFilter, List<KeepingDeviceDTO> devices);

    public CompletableFuture<List<KeepingDeviceDTO>> getPageForKeepingDevices(List<KeepingDeviceDTO> sourceList, int pageIndex, int pageSize);
}
