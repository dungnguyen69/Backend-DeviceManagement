package com.fullstack.Backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.fullstack.Backend.constant.constant.*;
import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.dto.device.DeviceFilterDTO;
import com.fullstack.Backend.dto.device.DeviceUpdateDTO;
import com.fullstack.Backend.exception.ResourceNotFoundException;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.FilterDeviceResponse;
import com.fullstack.Backend.responses.UpdateDeviceResponse;
import com.fullstack.Backend.services.IDeviceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

	@Autowired
	IDeviceService _deviceService;

	@GetMapping("/warehouse")
	public ResponseEntity<Object> getAllDevices(
			@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir,
			DeviceFilterDTO deviceFilterDTO) {
		DeviceInWarehouseResponse deviceResponse = _deviceService.getAllDevicesWithPaging(pageNo, pageSize, sortBy,
				sortDir, deviceFilterDTO);
		if (deviceResponse.getTotalElements() != EMPTY_LIST) {
			return new ResponseEntity<>(deviceResponse, OK);
		} else {
			return new ResponseEntity<>(NOT_FOUND);
		}
	}

	@GetMapping("/warehouse/{id}")
	public ResponseEntity<Object> getDetailDevice(@PathVariable(value = "id") int deviceId) {
		DetailDeviceResponse deviceResponse = _deviceService.getDetailDevice(deviceId);
		if (deviceResponse.getDetailDevice() != null) {
			return new ResponseEntity<>(deviceResponse, OK);
		} else {
			throw new ResourceNotFoundException("Device with Id is " + deviceId + " is not exist");
		}
	}

	@PostMapping("/warehouse")
	@ResponseBody
	public ResponseEntity<Object> addANewDevice(@Valid @RequestBody DeviceAddDTO device) {
		AddDeviceResponse deviceResponse = _deviceService.addANewDevice(device);
		return new ResponseEntity<>(deviceResponse, OK);
	}

	@PutMapping("/warehouse/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateDevice(@PathVariable(value = "id") int deviceId,
			@Valid @RequestBody DeviceUpdateDTO device) {
		UpdateDeviceResponse deviceResponse = _deviceService.updateDevice(deviceId, device);
		return new ResponseEntity<>(deviceResponse, OK);
	}

	@GetMapping("/warehouse/suggest")
	@ResponseBody
	public ResponseEntity<Object> getSuggestKeywordDevices(@RequestParam(name = "column") int fieldColumn,
			@RequestParam(name = "keyword") String keyword, DeviceFilterDTO device) {
		FilterDeviceResponse deviceResponse = _deviceService.getSuggestKeywordDevices(fieldColumn, keyword, device);
		return new ResponseEntity<>(deviceResponse, OK);
	}
}
