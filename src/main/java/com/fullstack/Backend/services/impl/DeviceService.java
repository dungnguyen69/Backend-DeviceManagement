package com.fullstack.Backend.services.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import static com.fullstack.Backend.constant.constant.*;
import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.dto.device.DeviceFilterDTO;
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
import com.fullstack.Backend.responses.FilterDeviceResponse;
import com.fullstack.Backend.responses.UpdateDeviceResponse;
import com.fullstack.Backend.services.IDeviceService;
import com.fullstack.Backend.services.IEmployeeService;
import com.fullstack.Backend.specifications.DeviceSearchCriteria;
import com.fullstack.Backend.specifications.DeviceSpecification;

@Service
public class DeviceService implements IDeviceService {

	@Autowired
	IDeviceRepository _deviceRepository;

	@Autowired
	IEmployeeService _employeeService;

	@Override
	public DeviceInWarehouseResponse getAllDevicesWithPaging(int pageIndex, int pageSize, String sortBy, String sortDir,
			DeviceFilterDTO deviceFilterDTO) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
		formatFilter(deviceFilterDTO);
		final DeviceSpecification specification = new DeviceSpecification(deviceFilterDTO);
		List<Device> devices = _deviceRepository.findAll(specification, pageable.getSort());
		final int start = (int) pageable.getOffset();
		final int end = Math.min((start + pageable.getPageSize()), devices.size());
		Page<Device> devicesWithPagination = new PageImpl<>(devices.subList(start, end), pageable, devices.size());
		List<DeviceDTO> deviceList = new ArrayList<DeviceDTO>();
		for (var device : devicesWithPagination) {
			DeviceDTO deviceDTO = new DeviceDTO();
			deviceDTO.loadFromEntity(device);
			deviceList.add(deviceDTO);
		}
		DeviceInWarehouseResponse deviceResponse = new DeviceInWarehouseResponse();
		deviceResponse.setDevicesList(deviceList);
		deviceResponse.setPageNo(pageIndex);
		deviceResponse.setPageSize(pageSize);
		deviceResponse.setTotalElements(devicesWithPagination.getTotalElements());
		deviceResponse.setTotalPages(devicesWithPagination.getTotalPages());
		return deviceResponse;
	}

	@Override
	public AddDeviceResponse addANewDevice(DeviceAddDTO deviceAddDTO) {
		try {
			User owner = _employeeService.findById(deviceAddDTO.getOwnerId());
			AddDeviceResponse addDeviceResponse = new AddDeviceResponse();
			Device device = new Device();
			device.loadFromEntity(deviceAddDTO);
			device.setOwner_Id(owner.getId());
			device.setCreatedDate(new Date());
			_deviceRepository.save(device);
			addDeviceResponse.setNewDevice(device);
			addDeviceResponse.setIsAddedSuccessful(true);
			return addDeviceResponse;
		} catch (Exception e) {
			if (e instanceof NoSuchElementException) {
				throw new NoSuchElementException("Owner does not exist", e);
			}
			if (e instanceof DataIntegrityViolationException) {
				throw new DataIntegrityViolationException(
						((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);
			}
		}
		return null;
	}

	@Override
	public DetailDeviceResponse getDetailDevice(int deviceId) {
		DetailDeviceResponse detailDeviceResponse = new DetailDeviceResponse();
		Device deviceDetail = _deviceRepository.findById(deviceId);
		if (deviceDetail != null) {
			User owner = _employeeService.findById(deviceDetail.getOwner_Id());
			DeviceUpdateDTO device = new DeviceUpdateDTO();
			device.loadFromEntity(deviceDetail);
			device.setOwnerId(owner.getId());
			detailDeviceResponse.setDetailDevice(device);
		} else {
			detailDeviceResponse.setDetailDevice(null);
		}
		return detailDeviceResponse;
	}

	@Override
	public UpdateDeviceResponse updateDevice(int deviceId, DeviceUpdateDTO device) {
		UpdateDeviceResponse detailDeviceResponse = new UpdateDeviceResponse();
		Device deviceDetail = _deviceRepository.findById(deviceId);
		try {
			if (deviceDetail != null) {
				User owner = _employeeService.findById(deviceDetail.getOwner_Id());
				deviceDetail.setName(device.getName().trim());
				deviceDetail.setStatus(Status.values()[device.getStatusId()]);
				deviceDetail.setSerialNumber(device.getSerialNumber().trim());
				deviceDetail.setInventoryNumber(device.getInventoryNumber().trim());
				deviceDetail.setProject(Project.values()[device.getProjectId()]);
				deviceDetail.setOrigin(Origin.values()[device.getOriginId()]);
				deviceDetail.setPlatform_Id(device.getPlatformId());
				deviceDetail.setRam_Id(device.getRamId());
				deviceDetail.setItem_type_Id(device.getItemTypeId());
				deviceDetail.setStorage_Id(device.getStorageId());
				deviceDetail.setScreen_Id(device.getScreenId());
				deviceDetail.setComments(device.getComments());
				deviceDetail.setOwner_Id(owner.getId());
				deviceDetail.setUpdatedDate(new Date());
				_deviceRepository.save(deviceDetail);
				detailDeviceResponse.setUpdatedDevice(deviceDetail);
			} else {
				detailDeviceResponse.setUpdatedDevice(null);
			}
		} catch (Exception e) {
			if (e instanceof NoSuchElementException) {
				throw new NoSuchElementException("Owner does not exist", e);
			} else if (e instanceof DataIntegrityViolationException) {
				throw new DataIntegrityViolationException(
						((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);
			}
		}
		return detailDeviceResponse;
	}

	@Override
	public FilterDeviceResponse getSuggestKeywordDevices(int fieldColumn, String keyword,
			DeviceFilterDTO deviceFilter) {
		final DeviceSpecification specification = new DeviceSpecification(deviceFilter);
		List<Device> devices = _deviceRepository.findAll(specification);
		List<String> keywordList = new ArrayList<>();
		switch (fieldColumn) {
		case DEVICE_NAME_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getName())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_PLATFORM_NAME_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getPlatform().getName())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_PLATFORM_VERSION_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getPlatform().getVersion())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_RAM_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getRam().getSize().toString())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_SCREEN_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getScreen().getSize().toString())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_STORAGE_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getStorage().getSize().toString())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_OWNER_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getOwner().getUserName())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_INVENTORY_NUMBER_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getInventoryNumber())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		case DEVICE_SERIAL_NUMBER_COLUMN:
			if (keyword != null) {
				keywordList = devices.stream().map(cell -> cell.getSerialNumber())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			}
			break;
		}
		FilterDeviceResponse response = new FilterDeviceResponse();
		response.setKeywordList(keywordList);
		return response;
	}

	@Override
	public void formatFilter(DeviceFilterDTO deviceFilterDTO) {
		if (deviceFilterDTO.getName() != null) {
			deviceFilterDTO.setName(deviceFilterDTO.getName().trim().toLowerCase());
		}
		if (deviceFilterDTO.getPlatformName() != null) {
			deviceFilterDTO.setPlatformName(deviceFilterDTO.getPlatformName().trim().toLowerCase());
		}
		if (deviceFilterDTO.getPlatformVersion() != null) {
			deviceFilterDTO.setPlatformVersion(deviceFilterDTO.getPlatformVersion().trim().toLowerCase());
		}
		if (deviceFilterDTO.getRam() != null) {
			deviceFilterDTO.setRam(deviceFilterDTO.getRam().trim().toLowerCase());
		}
		if (deviceFilterDTO.getScreen() != null) {
			deviceFilterDTO.setScreen(deviceFilterDTO.getScreen().trim().toLowerCase());
		}
		if (deviceFilterDTO.getStorage() != null) {
			deviceFilterDTO.setStorage(deviceFilterDTO.getStorage().trim().toLowerCase());
		}
		if (deviceFilterDTO.getInventoryNumber() != null) {
			deviceFilterDTO.setInventoryNumber(deviceFilterDTO.getInventoryNumber().trim().toLowerCase());
		}
		if (deviceFilterDTO.getSerialNumber() != null) {
			deviceFilterDTO.setSerialNumber(deviceFilterDTO.getSerialNumber().trim().toLowerCase());
		}
		if (deviceFilterDTO.getOwner() != null) {
			deviceFilterDTO.setOwner(deviceFilterDTO.getOwner().trim().toLowerCase());
		}
	}
}
