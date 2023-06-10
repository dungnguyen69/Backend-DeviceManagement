package com.fullstack.Backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.fullstack.Backend.constant.constant.*;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.FilterDeviceDTO;
import com.fullstack.Backend.dto.device.UpdateDeviceDTO;
import com.fullstack.Backend.exception.ResourceNotFoundException;
import com.fullstack.Backend.responses.device.DeleteDeviceResponse;
import com.fullstack.Backend.responses.device.DetailDeviceResponse;
import com.fullstack.Backend.responses.device.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.device.DropdownValuesResponse;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.services.IDeviceService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    IDeviceService _deviceService;

    @GetMapping("/warehouse")
    public ResponseEntity<Object> showDevicesWithPaging(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @DateTimeFormat(pattern = "yyyy-MM-dd") FilterDeviceDTO deviceFilterDTO) throws InterruptedException, ExecutionException {
        CompletableFuture<DeviceInWarehouseResponse> deviceResponse = _deviceService.showDevicesWithPaging(pageNo,
                pageSize, sortBy, sortDir, deviceFilterDTO);
        if (deviceResponse.get().getTotalElements() != EMPTY_LIST)
            return new ResponseEntity<>(deviceResponse.get(), OK);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<Object> getDetailDevice(@PathVariable(value = "id") int deviceId)
            throws InterruptedException, ExecutionException {
        CompletableFuture<DetailDeviceResponse> deviceResponse = _deviceService.getDetailDevice(deviceId);
        if (deviceResponse.get().getDetailDevice() != null)
            return new ResponseEntity<>(deviceResponse.get(), OK);
        throw new ResourceNotFoundException("Device with Id is " + deviceId + " is not exist");
    }

    @PostMapping("/warehouse")
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> addANewDevice(@Valid @RequestBody AddDeviceDTO device)
            throws InterruptedException, ExecutionException {
        return _deviceService.addANewDevice(device);
    }

    @PutMapping("/warehouse/{id}")
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> updateDevice(@PathVariable(value = "id") int deviceId,
                                                                  @Valid @RequestBody UpdateDeviceDTO device) throws InterruptedException, ExecutionException {
        return _deviceService.updateDevice(deviceId, device);
    }

    @GetMapping("/warehouse/suggestion")
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordDevices(@RequestParam(name = "column") int fieldColumn,
                                                                              @RequestParam(name = "keyword") String keyword, FilterDeviceDTO device)
            throws InterruptedException, ExecutionException {
        return _deviceService.getSuggestKeywordDevices(fieldColumn,
                keyword, device);
    }

    @DeleteMapping("/warehouse/{id}")
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> deleteDevice(@PathVariable(value = "id") int deviceId)
            throws InterruptedException, ExecutionException {
        return _deviceService.deleteADevice(deviceId);
    }

    @GetMapping("/warehouse/export/excel")
    @ResponseBody
    public void exportToExcel(HttpServletResponse response) throws IOException {
        _deviceService.exportToExcel(response);
    }

    @GetMapping("/warehouse/download-template")
    @ResponseBody
    public void downloadTemplateImport(HttpServletResponse response)
            throws IOException, InterruptedException, ExecutionException {
        _deviceService.downloadTemplate(response);
    }

    @PostMapping("/warehouse/import")
    @ResponseBody
    public CompletableFuture<ResponseEntity<Object>> importFile(@RequestParam("file") MultipartFile file)
            throws IOException, InterruptedException, ExecutionException {
        return _deviceService.importToDb(file);
    }

    @GetMapping("/warehouse/drop-down-values")
    @ResponseBody
    public CompletableFuture<DropdownValuesResponse> getDropdownValues()
            throws IOException, InterruptedException, ExecutionException {
        return _deviceService.getDropDownValues();
    }
}
