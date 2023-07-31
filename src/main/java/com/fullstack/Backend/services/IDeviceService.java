package com.fullstack.Backend.services;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.fullstack.Backend.dto.device.*;
import com.fullstack.Backend.dto.request.ReturnKeepDeviceDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.responses.device.DetailDeviceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.fullstack.Backend.responses.device.DropdownValuesResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface IDeviceService {
    public CompletableFuture<Optional<Device>> getDeviceById(int deviceId);

    public CompletableFuture<ResponseEntity<Object>> showDevicesWithPaging(int pageIndex, int pageSize, String sortBy, String sortDir, FilterDeviceDTO dto) throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> addDevice(AddDeviceDTO dto) throws ExecutionException, InterruptedException;

    public DetailDeviceResponse getDetailDevice(int deviceId) throws InterruptedException, ExecutionException;

    public ResponseEntity<Object> updateDevice(int deviceId, UpdateDeviceDTO device) throws ExecutionException, InterruptedException;

    public CompletableFuture<ResponseEntity<Object>> deleteDevice(int deviceId) throws ExecutionException, InterruptedException;

    public void exportToExcel(HttpServletResponse response) throws IOException, ExecutionException, InterruptedException;

    public void exportToExcelForOwner(int ownerId, HttpServletResponse response) throws IOException, ExecutionException, InterruptedException;

    public void downloadTemplate(HttpServletResponse response) throws IOException, InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> importToDb(int ownerId, MultipartFile file) throws Exception;

    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordDevices(int fieldColumn, String keyword, FilterDeviceDTO dto) throws InterruptedException, ExecutionException;

    public CompletableFuture<DropdownValuesResponse> getDropDownValues() throws InterruptedException, ExecutionException;

    public CompletableFuture<Boolean> doesDeviceExist(int deviceId);

    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordOwnedDevices(int ownerId, int fieldColumn, String keyword, FilterDeviceDTO dto) throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordKeepingDevices(int keeperId, int fieldColumn, String keyword, FilterDeviceDTO dto) throws InterruptedException, ExecutionException;

    public CompletableFuture<ResponseEntity<Object>> updateReturnKeepDevice(ReturnKeepDeviceDTO request) throws ExecutionException, InterruptedException, ParseException;

    public CompletableFuture<ResponseEntity<Object>> updateReturnOwnedDevice(ReturnKeepDeviceDTO request) throws ExecutionException, InterruptedException, ParseException;

    public CompletableFuture<ResponseEntity<Object>> showOwnedDevicesWithPaging(int ownerId, int pageIndex, int pageSize, String sortBy, String sortDir, FilterDeviceDTO dto) throws ExecutionException, InterruptedException;

    public CompletableFuture<ResponseEntity<Object>> showKeepingDevicesWithPaging(int keeperId, int pageIndex, int pageSize, FilterDeviceDTO dto) throws ExecutionException, InterruptedException;
}
