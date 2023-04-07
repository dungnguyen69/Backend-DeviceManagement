package com.fullstack.Backend.services.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.dto.device.DeviceUpdateDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;
import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;
import com.fullstack.Backend.services.IDeviceService;
import com.fullstack.Backend.services.IEmployeeService;

@Service
public class DeviceService implements IDeviceService {

	@Autowired
	IDeviceRepository _deviceRepository;

	@Autowired
	IEmployeeService _employeeService;

	@Override
	public DeviceInWarehouseResponse getAllDevices(int pageIndex, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
		Page<Device> devices = _deviceRepository.findAll(pageable);
		List<DeviceDTO> deviceList = new ArrayList<DeviceDTO>();
		for (var device : devices) {
			DeviceDTO deviceDTO = new DeviceDTO();
			deviceDTO.loadFromEntity(device);
			deviceList.add(deviceDTO);
		}
		DeviceInWarehouseResponse deviceResponse = new DeviceInWarehouseResponse();
		deviceResponse.setDevicesList(deviceList);
		deviceResponse.setPageNo(pageIndex);
		deviceResponse.setPageSize(pageSize);
		deviceResponse.setTotalElements(devices.getTotalElements());
		deviceResponse.setTotalPages(devices.getTotalPages());
		return deviceResponse;
	}

	@Override
	public AddDeviceResponse addANewDevice(DeviceAddDTO deviceAddDTO) {
		try {
			User owner = _employeeService.findById(deviceAddDTO.getOwnerId());
			AddDeviceResponse addDeviceResponse = new AddDeviceResponse();
			Device device = new Device();
			device.setName(deviceAddDTO.getName().trim());
			device.setStatus(Status.values()[deviceAddDTO.getStatusId() - 1]);
			device.setSerialNumber(deviceAddDTO.getSerialNumber().trim());
			device.setInventoryNumber(deviceAddDTO.getInventoryNumber().trim());
			device.setProject(Project.values()[deviceAddDTO.getProjectId() - 1]);
			device.setOrigin(Origin.values()[deviceAddDTO.getOriginId() - 1]);
			device.setPlatform_Id(deviceAddDTO.getPlatformId());
			device.setRam_Id(deviceAddDTO.getRamId());
			device.setItem_type_Id(deviceAddDTO.getItemTypeId());
			device.setStorage_Id(deviceAddDTO.getStorageId());
			device.setScreen_Id(deviceAddDTO.getScreenId());
			device.setComments(deviceAddDTO.getComments());
			device.setOwner_Id(owner.getId());
			device.setCreatedDate(new Date());
			_deviceRepository.save(device);
			addDeviceResponse.setNewDevice(device);
			addDeviceResponse.setIsAddedSuccessful(true);
			return addDeviceResponse;
		} catch (Exception e) {
			if (e instanceof NoSuchElementException) {
				throw new NoSuchElementException("Owner does not exist", e);
			} else if (e instanceof DataIntegrityViolationException) {
				throw new DataIntegrityViolationException(
						((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public DetailDeviceResponse getDetailDevice(int deviceId) {
		DetailDeviceResponse detailDeviceResponse = new DetailDeviceResponse();
		Device deviceDetail = _deviceRepository.findById(deviceId);
		if (deviceDetail != null) {
			User owner = _employeeService.findById(deviceDetail.getOwner_Id());
			DeviceUpdateDTO deviceUpdateDTO = new DeviceUpdateDTO();
			deviceUpdateDTO.setId(deviceDetail.getId());
			deviceUpdateDTO.setName(deviceDetail.getName().trim());
			deviceUpdateDTO.setStatusId(Status.valueOf(deviceDetail.getStatus().toString()).ordinal());
			deviceUpdateDTO.setSerialNumber(deviceDetail.getSerialNumber().trim());
			deviceUpdateDTO.setInventoryNumber(deviceDetail.getInventoryNumber().trim());
			deviceUpdateDTO.setProjectId(Project.valueOf(deviceDetail.getProject().toString()).ordinal());
			deviceUpdateDTO.setOriginId(Origin.valueOf(deviceDetail.getOrigin().toString()).ordinal());
			deviceUpdateDTO.setPlatformId(deviceDetail.getPlatform_Id());
			deviceUpdateDTO.setRamId(deviceDetail.getRam_Id());
			deviceUpdateDTO.setItemTypeId(deviceDetail.getItem_type_Id());
			deviceUpdateDTO.setStorageId(deviceDetail.getStorage_Id());
			deviceUpdateDTO.setScreenId(deviceDetail.getScreen_Id());
			deviceUpdateDTO.setComments(deviceDetail.getComments());
			deviceUpdateDTO.setOwnerId(owner.getId());
			deviceUpdateDTO.setCreatedDate(deviceDetail.getCreatedDate());
			deviceUpdateDTO.setUpdatedDate(deviceDetail.getUpdatedDate());
			detailDeviceResponse.setDetailDevice(deviceUpdateDTO);
		} else {
			detailDeviceResponse.setDetailDevice(null);
		}
		return detailDeviceResponse;

	}

	@Override
	public Device updateDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device filterDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}
