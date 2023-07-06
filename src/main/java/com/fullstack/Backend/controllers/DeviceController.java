package com.fullstack.Backend.controllers;

import com.fullstack.Backend.dto.request.ReturnKeepDeviceDTO;
import com.fullstack.Backend.services.IRequestService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.fullstack.Backend.constant.constant.*;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.FilterDeviceDTO;
import com.fullstack.Backend.dto.device.UpdateDeviceDTO;
import com.fullstack.Backend.responses.device.DropdownValuesResponse;
import com.fullstack.Backend.services.IDeviceService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    IDeviceService _deviceService;
    @Autowired
    IRequestService _requestService;

    @GetMapping("/warehouse")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> showDevicesWithPaging(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) FilterDeviceDTO deviceFilterDTO) throws InterruptedException, ExecutionException {
        return _deviceService.showDevicesWithPaging(pageNo, pageSize, sortBy, sortDir, deviceFilterDTO);
    }

    @GetMapping("/warehouse/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getDetailDevice(
            @PathVariable(value = "id") int deviceId)
            throws InterruptedException, ExecutionException {
        return _deviceService.getDetailDevice(deviceId);
    }

    @PostMapping("/warehouse")
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> addANewDevice(
            @Valid @RequestBody AddDeviceDTO device)
            throws InterruptedException, ExecutionException {
        return _deviceService.addDevice(device);
    }

    @PutMapping("/warehouse/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> updateDevice(
            @PathVariable(value = "id") int deviceId,
            @Valid @RequestBody UpdateDeviceDTO device) throws InterruptedException, ExecutionException {
        return _deviceService.updateDevice(deviceId, device);
    }

    @GetMapping("/warehouse/suggestion")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordDevices(
            @RequestParam(name = "column") int fieldColumn,
            @RequestParam(name = "keyword") String keyword,
            FilterDeviceDTO device)
            throws InterruptedException, ExecutionException {
        return _deviceService.getSuggestKeywordDevices(fieldColumn,
                keyword, device);
    }

    @DeleteMapping("/warehouse/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> deleteDevice(
            @PathVariable(value = "id") int deviceId)
            throws InterruptedException, ExecutionException {
        return _deviceService.deleteDevice(deviceId);
    }

    @GetMapping("/warehouse/export")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {
        _deviceService.exportToExcel(response);
    }

    @GetMapping("/warehouse/download-template")
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void downloadTemplateImport(HttpServletResponse response)
            throws IOException, InterruptedException, ExecutionException {
        _deviceService.downloadTemplate(response);
    }

    @PostMapping(value = "/warehouse/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> importFile(
            @RequestParam("file") MultipartFile file)
            throws Exception {
        return _deviceService.importToDb(file);
    }

    @GetMapping("/warehouse/drop-down-values")
    @ResponseBody
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")

    public CompletableFuture<DropdownValuesResponse> getDropdownValues()
            throws IOException, InterruptedException, ExecutionException {
        return _deviceService.getDropDownValues();
    }

    @GetMapping("/owners/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getDevicesOfOwner(
            @PathVariable(value = "id") int ownerId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @DateTimeFormat(pattern = "yyyy-MM-dd") FilterDeviceDTO deviceFilterDTO)
            throws IOException, InterruptedException, ExecutionException {
        return _deviceService.showOwnedDevicesWithPaging(ownerId, pageNo, pageSize, sortBy, sortDir, deviceFilterDTO);
    }

    @PutMapping("/keepers/return")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> updateReturnKeepDevice(
            @DateTimeFormat(pattern = "yyyy-MM-dd") ReturnKeepDeviceDTO request)
            throws InterruptedException, ExecutionException, ParseException {
        return _deviceService.updateReturnKeepDevice(request);
    }

    @GetMapping("/keepers/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getDevicesOfKeeper(
            @PathVariable(value = "id") int ownerId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @DateTimeFormat(pattern = "yyyy-MM-dd") FilterDeviceDTO deviceFilterDTO)
            throws IOException, InterruptedException, ExecutionException {
        return _deviceService.showKeepingDevicesWithPaging(ownerId, pageNo, pageSize, deviceFilterDTO);
    }

    @PutMapping("/owners/return")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> updateReturnOwnedDevice(
            @DateTimeFormat(pattern = "yyyy-MM-dd") ReturnKeepDeviceDTO request)
            throws InterruptedException, ExecutionException, ParseException {
        return _deviceService.updateReturnOwnedDevice(request);
    }

    @GetMapping("/owners/{id}/suggestion")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordOwnedDevices(
            @PathVariable(value = "id") int ownerId,
            @RequestParam(name = "column") int fieldColumn,
            @RequestParam(name = "keyword") String keyword, FilterDeviceDTO device)
            throws InterruptedException, ExecutionException {
        return _deviceService.getSuggestKeywordOwnedDevices(ownerId, fieldColumn,
                keyword, device);
    }

    @GetMapping("/keepers/{id}/suggestion")
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordKeepingDevices(
            @PathVariable(value = "id") int keeperId,
            @RequestParam(name = "column") int fieldColumn,
            @RequestParam(name = "keyword") String keyword, FilterDeviceDTO device)
            throws InterruptedException, ExecutionException {
        return _deviceService.getSuggestKeywordKeepingDevices(keeperId, fieldColumn,
                keyword, device);
    }
}
