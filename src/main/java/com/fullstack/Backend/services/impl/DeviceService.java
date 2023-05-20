package com.fullstack.Backend.services.impl;

import java.util.Date;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.dto.device.DeviceFilterDTO;
import com.fullstack.Backend.dto.device.DeviceUpdateDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.entities.Platform;
import com.fullstack.Backend.entities.Ram;
import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.entities.Storage;
import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;
import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DeleteDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.FilterDeviceResponse;
import com.fullstack.Backend.responses.UpdateDeviceResponse;
import com.fullstack.Backend.services.IDeviceService;
import com.fullstack.Backend.services.IEmployeeService;
import com.fullstack.Backend.services.IItemTypeService;
import com.fullstack.Backend.services.IPlatformService;
import com.fullstack.Backend.services.IRamService;
import com.fullstack.Backend.services.IScreenService;
import com.fullstack.Backend.services.IStorageService;
import com.fullstack.Backend.specifications.DeviceSpecification;
import com.fullstack.Backend.utils.DeviceExcelExporter;
import com.fullstack.Backend.utils.DeviceExcelImporter;
import com.fullstack.Backend.utils.DeviceExcelTemplate;
import com.fullstack.Backend.utils.DropDownListsDTO;
import com.fullstack.Backend.utils.ImportError;
import com.fullstack.Backend.responses.ImportDeviceResponse;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class DeviceService implements IDeviceService {

	@Autowired
	IDeviceRepository _deviceRepository;

	@Autowired
	IItemTypeService _itemTypeService;

	@Autowired
	IRamService _ramService;

	@Autowired
	IPlatformService _platformService;

	@Autowired
	IScreenService _screenService;

	@Autowired
	IStorageService _storageService;

	@Autowired
	IEmployeeService _employeeService;

	@Async
	@Override
	public CompletableFuture<DeviceInWarehouseResponse> showDevicesWithPaging(int pageIndex, int pageSize,
			String sortBy, String sortDir, DeviceFilterDTO deviceFilterDTO) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
		formatFilter(deviceFilterDTO);
		final DeviceSpecification specification = new DeviceSpecification(deviceFilterDTO);
		Page<Device> devices = _deviceRepository.findAll(specification, pageable);
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
		return CompletableFuture.completedFuture(deviceResponse);
	}

	@Async
	@Override
	public CompletableFuture<AddDeviceResponse> addANewDevice(DeviceAddDTO deviceAddDTO) {
		AddDeviceResponse addDeviceResponse = new AddDeviceResponse();
		try {
			CompletableFuture<User> owner = _employeeService.findById(deviceAddDTO.getOwnerId());
			if (owner.get() != null) {
				Device device = new Device();
				device.loadToEntity(deviceAddDTO);
				_deviceRepository.save(device);
				addDeviceResponse.setNewDevice(device);
				addDeviceResponse.setIsAddedSuccessful(true);
			}
		} catch (Exception e) {
			if (e instanceof NoSuchElementException)
				throw new NoSuchElementException("Owner does not exist", e);
			if (e instanceof DataIntegrityViolationException)
				throw new DataIntegrityViolationException(
						((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);
		}
		return CompletableFuture.completedFuture(addDeviceResponse);
	}

	// What happens if a device's user is vanished ?
	@Async
	@Override
	public CompletableFuture<DetailDeviceResponse> getDetailDevice(int deviceId)
			throws InterruptedException, ExecutionException {
		DetailDeviceResponse detailDeviceResponse = new DetailDeviceResponse();
		Device deviceDetail = _deviceRepository.findById(deviceId);
		if (deviceDetail == null)
			return CompletableFuture.completedFuture(detailDeviceResponse);

		CompletableFuture<User> owner = _employeeService.findById(deviceDetail.getOwner_Id());
		DeviceUpdateDTO deviceUpdateDTO = new DeviceUpdateDTO();
		if (owner == null)
			deviceUpdateDTO.setOwnerId("");
		else
			deviceUpdateDTO.setOwnerId(String.valueOf(owner.get().getId()));
		deviceUpdateDTO.loadFromEntity(deviceDetail);
		detailDeviceResponse.setDetailDevice(deviceUpdateDTO);
		return CompletableFuture.completedFuture(detailDeviceResponse);
	}

	@Async()
	@Override
	public CompletableFuture<UpdateDeviceResponse> updateDevice(int deviceId, DeviceUpdateDTO device) {
		UpdateDeviceResponse detailDeviceResponse = new UpdateDeviceResponse();
		Device deviceDetail = _deviceRepository.findById(deviceId);
		try {
			CompletableFuture<User> owner = _employeeService.findById(Integer.valueOf(device.getOwnerId()));
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
			deviceDetail.setOwner_Id(Integer.valueOf(owner.get().getId()));
			deviceDetail.setUpdatedDate(new Date());
			_deviceRepository.save(deviceDetail);
			detailDeviceResponse.setUpdatedDevice(deviceDetail);
		} catch (Exception e) {
			if (e instanceof NoSuchElementException)
				throw new NoSuchElementException("Owner does not exist", e);
			if (e instanceof DataIntegrityViolationException)
				throw new DataIntegrityViolationException(
						((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);

		}
		return CompletableFuture.completedFuture(detailDeviceResponse);
	}

	@Async()
	@Override
	public CompletableFuture<FilterDeviceResponse> getSuggestKeywordDevices(int fieldColumn, String keyword,
			DeviceFilterDTO deviceFilter) {
		formatFilter(deviceFilter);
		List<String> keywordList = new ArrayList<>();
		final DeviceSpecification specification = new DeviceSpecification(deviceFilter);
		List<Device> devices = _deviceRepository.findAll(specification);
		switch (fieldColumn) {
		case DEVICE_NAME_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getName())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_PLATFORM_NAME_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getPlatform().getName())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_PLATFORM_VERSION_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getPlatform().getVersion())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_RAM_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getRam().getSize().toString())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_SCREEN_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getScreen().getSize().toString())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_STORAGE_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getStorage().getSize().toString())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_OWNER_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getOwner().getUserName())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_INVENTORY_NUMBER_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getInventoryNumber())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		case DEVICE_SERIAL_NUMBER_COLUMN:
			if (keyword != null)
				keywordList = devices.stream().map(cell -> cell.getSerialNumber())
						.filter(cell -> cell.toLowerCase().contains(keyword)).distinct().collect(Collectors.toList());
			break;
		}
		FilterDeviceResponse response = new FilterDeviceResponse();
		response.setKeywordList(keywordList);
		return CompletableFuture.completedFuture(response);
	}

	@Override
	public void formatFilter(DeviceFilterDTO deviceFilterDTO) {
		if (deviceFilterDTO.getName() != null)
			deviceFilterDTO.setName(deviceFilterDTO.getName().trim().toLowerCase());

		if (deviceFilterDTO.getPlatformName() != null)
			deviceFilterDTO.setPlatformName(deviceFilterDTO.getPlatformName().trim().toLowerCase());

		if (deviceFilterDTO.getPlatformVersion() != null)
			deviceFilterDTO.setPlatformVersion(deviceFilterDTO.getPlatformVersion().trim().toLowerCase());

		if (deviceFilterDTO.getRam() != null)
			deviceFilterDTO.setRam(deviceFilterDTO.getRam().trim().toLowerCase());

		if (deviceFilterDTO.getScreen() != null)
			deviceFilterDTO.setScreen(deviceFilterDTO.getScreen().trim().toLowerCase());

		if (deviceFilterDTO.getStorage() != null)
			deviceFilterDTO.setStorage(deviceFilterDTO.getStorage().trim().toLowerCase());

		if (deviceFilterDTO.getInventoryNumber() != null)
			deviceFilterDTO.setInventoryNumber(deviceFilterDTO.getInventoryNumber().trim().toLowerCase());

		if (deviceFilterDTO.getSerialNumber() != null)
			deviceFilterDTO.setSerialNumber(deviceFilterDTO.getSerialNumber().trim().toLowerCase());

		if (deviceFilterDTO.getOwner() != null)
			deviceFilterDTO.setOwner(deviceFilterDTO.getOwner().trim().toLowerCase());

	}

	@Async
	@Override
	public CompletableFuture<DeleteDeviceResponse> deleteADevice(int deviceId) {
		DeleteDeviceResponse response = new DeleteDeviceResponse();
		if (_deviceRepository.findById(deviceId) == null)
			return CompletableFuture.completedFuture(response);

		_deviceRepository.deleteById((long) deviceId);
		response.setIsDeletionSuccessful(true);
		return CompletableFuture.completedFuture(response);
	}

	@Async()
	@Override
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream"); // ?
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // format for date
		String currentDateTime = dateFormatter.format(new Date()); // attain current date

		String headerKey = "Content-Disposition"; // ?
		String headerValue = "attachment; filename=ExportDevices_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);
		List<Device> devices = _deviceRepository.findAll();
		DeviceExcelExporter excelExporter = new DeviceExcelExporter(devices);
		excelExporter.export(response);
	}

	@Async()
	@Override
	public void downloadTemplate(HttpServletResponse response)
			throws IOException, InterruptedException, ExecutionException {
		response.setContentType("application/octet-stream"); // ?
		String headerKey = "Content-Disposition"; // ?
		String headerValue = "attachment; filename=Template_Import.xlsx";
		response.setHeader(headerKey, headerValue);

		String[] statusList = Stream.of(Status.values()).map(Status::name).toArray(String[]::new);
		String[] projectList = Stream.of(Project.values()).map(Project::name).toArray(String[]::new);
		String[] originList = Stream.of(Origin.values()).map(Origin::name).toArray(String[]::new);
		String[] itemTypeList = _itemTypeService.getItemTypeList().get().stream().toArray(String[]::new);
		String[] ramList = _ramService.getRamList().get().stream().toArray(String[]::new);
		String[] platformList = _platformService.getPlatformNameVersionList().get().stream().toArray(String[]::new);
		String[] screenList = _screenService.getScreenList().get().stream().toArray(String[]::new);
		String[] storageList = _storageService.getStorageList().get().stream().toArray(String[]::new);

		DropDownListsDTO dropDownListsDTO = new DropDownListsDTO();
		dropDownListsDTO.setStatusList(statusList);
		dropDownListsDTO.setProjectList(projectList);
		dropDownListsDTO.setOriginList(originList);
		dropDownListsDTO.setItemTypeList(itemTypeList);
		dropDownListsDTO.setRamList(ramList);
		dropDownListsDTO.setPlatformList(platformList);
		dropDownListsDTO.setScreenList(screenList);
		dropDownListsDTO.setStorageList(storageList);

		DeviceExcelTemplate deviceExcelTemplate = new DeviceExcelTemplate();
		deviceExcelTemplate.export(response, dropDownListsDTO);
	}

	@Async
	@Override
	public CompletableFuture<ResponseEntity<Object>> importToDb(MultipartFile file) throws IOException {
		List<Device> deviceList = new ArrayList<>();
		List<String> errors = new ArrayList<String>();
		String message = "";
		int rowIndex = 0, numberOfRows;

		if (DeviceExcelImporter.hasExcelFormat(file)) {
			if (!file.isEmpty()) {

				try {
					XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
					XSSFSheet sheet = workBook.getSheet("Devices");
					if (sheet == null)
						return CompletableFuture
								.completedFuture(new ResponseEntity<>("Sheet \"Devices\" is nonexistent", NOT_FOUND));
					numberOfRows = DeviceExcelImporter.getNumberOfNonEmptyCells(sheet, 0);
					if (numberOfRows == 0)
						return CompletableFuture.completedFuture(
								new ResponseEntity<>("Sheet must be not empty", HttpStatus.BAD_REQUEST));

					for (; rowIndex < numberOfRows; rowIndex++) {
						if (rowIndex == 0)
							continue;

						Row currentRow = sheet.getRow(rowIndex);
						String[] platformString = currentRow.getCell(DEVICE_PLATFORM).toString().split(",");
						Device device;

						String name = currentRow.getCell(DEVICE_NAME).toString().strip(),
								inventoryNumber = currentRow.getCell(DEVICE_INVENTORY_NUMBER).toString().strip(),
								serialNumber = currentRow.getCell(DEVICE_SERIAL_NUMBER).toString().strip(),
								comments = currentRow.getCell(DEVICE_COMMENTS).toString(),
								platformName = platformString[0].strip(), platfornmVersion = platformString[1].strip();

						CompletableFuture<ItemType> itemType = _itemTypeService
								.findByName(currentRow.getCell(DEVICE_ITEM_TYPE).toString().strip());
						CompletableFuture<Platform> platform = _platformService.findByNameAndVersion(platformName,
								platfornmVersion);
						CompletableFuture<Ram> ram = _ramService
								.findBySize((int) currentRow.getCell(DEVICE_RAM).getNumericCellValue());
						CompletableFuture<Screen> screen = _screenService
								.findBySize((int) currentRow.getCell(DEVICE_SCREEN).getNumericCellValue());
						CompletableFuture<Storage> storage = _storageService
								.findBySize((int) currentRow.getCell(DEVICE_STORAGE).getNumericCellValue());
						CompletableFuture<User> owner = _employeeService
								.findByUsername(currentRow.getCell(DEVICE_OWNER).toString().strip());

						Status status = Status.valueOf(currentRow.getCell(DEVICE_STATUS).toString().strip());
						Origin origin = Origin.valueOf(currentRow.getCell(DEVICE_ORIGIN).toString().strip());
						Project project = Project.valueOf(currentRow.getCell(DEVICE_PROJECT).toString().strip());

						int rowInExcel = rowIndex + 1;
						if (name.isBlank()) {
							errors.add("Name at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (inventoryNumber.isBlank()) {
							errors.add("Inventory number at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (serialNumber.isBlank()) {
							errors.add("Serial number at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (ram.get() == null) {
							errors.add("Ram at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (itemType.get() == null) {
							errors.add("Item type at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (screen.get() == null) {
							errors.add("Screen at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (storage.get() == null) {
							errors.add("Storage at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (owner.get() == null) {
							errors.add("Owner at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (project == null) {
							errors.add("Project at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (origin == null) {
							errors.add("Origin at row " + rowInExcel + " must be mandatory");
							break;
						}
						if (status == null) {
							errors.add("Status at row " + rowInExcel + " must be mandatory");
							break;
						}
						Device existDevice = _deviceRepository.findBySerialNumber(serialNumber);

						// Update
						if (existDevice != null) {
							existDevice.setName(name);
							existDevice.setStatus(status);
							existDevice.setInventoryNumber(inventoryNumber);
							existDevice.setProject(project);
							existDevice.setOrigin(origin);
							existDevice.setPlatform_Id(platform.get().getId());
							existDevice.setRam_Id(ram.get().getId());
							existDevice.setItem_type_Id(itemType.get().getId());
							existDevice.setStorage_Id(storage.get().getId());
							existDevice.setScreen_Id(screen.get().getId());
							existDevice.setComments(comments);
							existDevice.setOwner_Id(owner.get().getId());
							existDevice.setUpdatedDate(new Date());
							deviceList.add(existDevice);
						}
						// Add
						else {
							device = Device.builder().name(name).status(status).ram_Id(ram.get().getId())
									.platform_Id(platform.get().getId()).screen_Id(screen.get().getId())
									.storage_Id(storage.get().getId()).owner_Id(owner.get().getId()).origin(origin)
									.project(project).comments(comments).item_type_Id(itemType.get().getId())
									.inventoryNumber(inventoryNumber).serialNumber(serialNumber).build();
							device.setCreatedDate(new Date());
							deviceList.add(device);
						}
					}
					workBook.close();
					// Display list of error fields
					if (!errors.isEmpty()) {
						ImportError importError = new ImportError(errors);
						return CompletableFuture
								.completedFuture(new ResponseEntity<Object>(importError, HttpStatus.BAD_REQUEST));
					}
				} catch (Exception e) {
					// For duplicate elements of inventory number
					throw new DataIntegrityViolationException(
							((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);
				}
			}

			try {
				_deviceRepository.saveAll(deviceList);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				ImportDeviceResponse importDevice = new ImportDeviceResponse(message);
				return CompletableFuture.completedFuture(new ResponseEntity<>(importDevice, OK));
			} catch (Exception e) {
				message = "Could not upload the file: " + e.getMessage() + "!";
				ImportDeviceResponse importDevice = new ImportDeviceResponse(message);
				return CompletableFuture.completedFuture(new ResponseEntity<>(importDevice, EXPECTATION_FAILED));
			}
		}

		message = "Please upload an excel file!";
		ImportDeviceResponse importDevice = new ImportDeviceResponse(message);
		return CompletableFuture.completedFuture(new ResponseEntity<>(importDevice, NOT_FOUND));
	}
}
