package com.fullstack.Backend.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.Backend.constant.constant;
import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.exception.ResourceNotFoundException;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;
import com.fullstack.Backend.services.IDeviceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

	@Autowired
	IDeviceService _deviceService;

	@GetMapping("/warehouse")
	public ResponseEntity<Object> getAllDevices(
			@RequestParam(defaultValue = constant.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(defaultValue = constant.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(defaultValue = constant.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = constant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		try {
			DeviceInWarehouseResponse deviceResponse = _deviceService.getAllDevices(pageNo, pageSize, sortBy, sortDir);
			if (deviceResponse.getTotalElements() != constant.EMPTY_LIST) {
				return new ResponseEntity<>(deviceResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/warehouse/{id}")
	public ResponseEntity<Object> getDetailDevice(@PathVariable(value = "id") int deviceId) {
		DetailDeviceResponse deviceResponse = _deviceService.getDetailDevice(deviceId);
		if (deviceResponse.getDetailDevice() != null) {
			return new ResponseEntity<>(deviceResponse, HttpStatus.OK);
		} else {
			throw new ResourceNotFoundException("Device with Id is " + deviceId + " is not exist");
		}
	}

	@PostMapping("/warehouse")
	@ResponseBody
	public ResponseEntity<Object> addANewDevice(@Valid @RequestBody DeviceAddDTO device) {
		AddDeviceResponse deviceResponse = _deviceService.addANewDevice(device);
		return new ResponseEntity<>(deviceResponse, HttpStatus.OK);
	}
}
