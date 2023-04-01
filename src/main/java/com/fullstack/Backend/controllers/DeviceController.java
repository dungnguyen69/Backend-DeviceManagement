package com.fullstack.Backend.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.services.IDeviceService;

@RestController
@RequestMapping("/api/v1")
public class DeviceController {
	
	@Autowired
	IDeviceService deviceService;
	
	 @GetMapping("/devices")
	  public ResponseEntity<List<Device>> getAllDevices() {
	    List<Device> devices = deviceService.getAllDevices();

	    if (!devices.isEmpty()) {
	      return new ResponseEntity<>(devices, HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }
}
