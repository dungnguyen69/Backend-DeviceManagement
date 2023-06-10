package com.fullstack.Backend.services.impl;

import java.util.Date;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fullstack.Backend.utils.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.*;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.dto.device.FilterDeviceDTO;
import com.fullstack.Backend.dto.device.UpdateDeviceDTO;
import com.fullstack.Backend.dto.device.DropDownListsDTO;
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
import com.fullstack.Backend.responses.device.AddDeviceResponse;
import com.fullstack.Backend.responses.device.DeleteDeviceResponse;
import com.fullstack.Backend.responses.device.DetailDeviceResponse;
import com.fullstack.Backend.responses.device.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.device.DropdownValuesResponse;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.device.UpdateDeviceResponse;
import com.fullstack.Backend.services.IDeviceService;
import com.fullstack.Backend.services.IEmployeeService;
import com.fullstack.Backend.services.IItemTypeService;
import com.fullstack.Backend.services.IPlatformService;
import com.fullstack.Backend.services.IRamService;
import com.fullstack.Backend.services.IScreenService;
import com.fullstack.Backend.services.IStorageService;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;
import com.fullstack.Backend.utils.dropdowns.OriginList;
import com.fullstack.Backend.utils.dropdowns.PlatformList;
import com.fullstack.Backend.utils.dropdowns.ProjectList;
import com.fullstack.Backend.utils.dropdowns.RamList;
import com.fullstack.Backend.utils.dropdowns.ScreenList;
import com.fullstack.Backend.utils.dropdowns.StatusList;
import com.fullstack.Backend.utils.dropdowns.StorageList;
import com.fullstack.Backend.responses.device.ImportDeviceResponse;
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
    public CompletableFuture<DeviceInWarehouseResponse> showDevicesWithPaging(int pageIndex, int pageSize, String sortBy, String sortDir, FilterDeviceDTO deviceFilter) throws InterruptedException, ExecutionException {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        formatFilter(deviceFilter);
        List<Device> devices = _deviceRepository.findAll(sort);
        devices = fetchFilteredDevice(deviceFilter, devices).get();
        List<String> statusList = devices.stream().map(c -> c.getStatus().name()).distinct().collect(Collectors.toList());
        List<String> originList = devices.stream().map(c -> c.getOrigin().name()).distinct().collect(Collectors.toList());
        List<String> projectList = devices.stream().map(c -> c.getProject().name()).distinct().collect(Collectors.toList());
        List<String> itemTypeList = devices.stream().map(c -> c.getItemType().getName()).distinct().collect(Collectors.toList());
        devices = getPage(devices, pageIndex, pageSize).get();
        List<DeviceDTO> deviceList = devices.stream().map(device -> new DeviceDTO(device)).collect(Collectors.toList());
        DeviceInWarehouseResponse deviceResponse = new DeviceInWarehouseResponse();
        deviceResponse.setDevicesList(deviceList);
        deviceResponse.setPageNo(pageIndex);
        deviceResponse.setPageSize(pageSize);
        deviceResponse.setTotalElements(deviceList.size());
        deviceResponse.setTotalPages(getTotalPages(pageSize, deviceList.size()));
        deviceResponse.setStatusList(statusList);
        deviceResponse.setOriginList(originList);
        deviceResponse.setProjectList(projectList);
        deviceResponse.setItemTypeList(itemTypeList);
        return CompletableFuture.completedFuture(deviceResponse);
    }

    @Async
    @Override
    public CompletableFuture<List<Device>> getPage(List<Device> sourceList, int pageIndex, int pageSize) {
        if (pageSize <= 0 || pageIndex <= 0) throw new IllegalArgumentException("invalid page size: " + pageSize);

        int fromIndex = (pageIndex - 1) * pageSize;

        if (sourceList == null || sourceList.size() <= fromIndex)
            return CompletableFuture.completedFuture(Collections.emptyList());

        return CompletableFuture.completedFuture(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));
    }

    @Async
    @Override
    public int getTotalPages(int pageSize, int listSize) {
        if (listSize == 0) return 1;

        if (listSize % pageSize == 0) return listSize / pageSize;

        return (listSize / pageSize) + 1;
    }

    @Async
    @Override //Display an error when adding a field's id that are out of range in the database
    public CompletableFuture<ResponseEntity<Object>> addANewDevice(AddDeviceDTO addDeviceDTO) throws ExecutionException, InterruptedException {
        AddDeviceResponse addDeviceResponse = new AddDeviceResponse();
        List<ErrorMessage> errors = new ArrayList<>();
        Boolean useNonExistent = !_employeeService.doesUserExist(addDeviceDTO.getOwnerId()).get(),
                isSerialNumberExistent = _deviceRepository.findBySerialNumber(addDeviceDTO.getSerialNumber()) != null,
                isItemTypeInvalid = !_itemTypeService.doesItemTypeExist(addDeviceDTO.getItemTypeId()).get(),
                isRamInvalid = !_ramService.doesRamExist(addDeviceDTO.getRamId()).get(),
                isStorageInvalid = !_storageService.doesStorageExist(addDeviceDTO.getStorageId()).get(),
                isScreenInvalid = !_screenService.doesScreenExist(addDeviceDTO.getScreenId()).get(),
                isPlatformInvalid = !_platformService.doesPlatformExist(addDeviceDTO.getPlatformId()).get(),
                isStatusInvalid = Status.findByNumber(addDeviceDTO.getStatusId()).isEmpty(),
                isOriginInvalid = Origin.findByNumber(addDeviceDTO.getOriginId()).isEmpty(),
                isProjectInvalid = Project.findByNumber(addDeviceDTO.getProjectId()).isEmpty();

        if (useNonExistent) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Owner does not exist");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isSerialNumberExistent) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Serial value number of this device is already existed");
            error.setStatusCode(BAD_REQUEST);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isItemTypeInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Item type value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isRamInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Ram value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isStorageInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Storage value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isScreenInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Screen value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isPlatformInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Platform value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isStatusInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Status value of this device is invalid");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isOriginInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Origin value of this device is invalid");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isProjectInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Project value of this device is invalid");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (errors.size() > 0)
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(errors, NOT_ACCEPTABLE));

        Device device = new Device();
        device.loadToEntity(addDeviceDTO);
        _deviceRepository.save(device);
        addDeviceResponse.setNewDevice(device);
        addDeviceResponse.setIsAddedSuccessful(true);
        return CompletableFuture.completedFuture(new ResponseEntity<Object>(addDeviceResponse, OK));
    }

    // What happens if a device's user is vanished ?
    @Async
    @Override
    public CompletableFuture<DetailDeviceResponse> getDetailDevice(int deviceId) throws InterruptedException, ExecutionException {
        DetailDeviceResponse detailDeviceResponse = new DetailDeviceResponse();
        Device deviceDetail = _deviceRepository.findById(deviceId);

        if (deviceDetail == null) return CompletableFuture.completedFuture(detailDeviceResponse);

        CompletableFuture<User> owner = _employeeService.findById(deviceDetail.getOwnerId());
        UpdateDeviceDTO deviceUpdateDTO = new UpdateDeviceDTO();

        if (owner == null) deviceUpdateDTO.setOwnerId(null);
        else deviceUpdateDTO.setOwnerId(owner.get().getId());

        deviceUpdateDTO.loadFromEntity(deviceDetail);
        detailDeviceResponse.setDetailDevice(deviceUpdateDTO);
        return CompletableFuture.completedFuture(detailDeviceResponse);
    }

    @Async()
    @Override
    public CompletableFuture<ResponseEntity<Object>> updateDevice(int deviceId, UpdateDeviceDTO device) throws ExecutionException, InterruptedException {
        UpdateDeviceResponse detailDeviceResponse = new UpdateDeviceResponse();
        List<ErrorMessage> errors = new ArrayList<>();
        if (!_deviceRepository.existsById((long) deviceId)) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Device does not exist");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(errors, NOT_ACCEPTABLE));
        }
        Device deviceDetail = _deviceRepository.findById(deviceId);
        Boolean useNonExistent = !_employeeService.doesUserExist(device.getOwnerId()).get(),
                isSerialNumberExistent = _deviceRepository.findBySerialNumber(device.getSerialNumber()) != null,
                isItemTypeInvalid = !_itemTypeService.doesItemTypeExist(device.getItemTypeId()).get(),
                isRamInvalid = !_ramService.doesRamExist(device.getRamId()).get(),
                isStorageInvalid = !_storageService.doesStorageExist(device.getStorageId()).get(),
                isScreenInvalid = !_screenService.doesScreenExist(device.getScreenId()).get(),
                isPlatformInvalid = !_platformService.doesPlatformExist(device.getPlatformId()).get(),
                isStatusInvalid = Status.findByNumber(device.getStatusId()).isEmpty(),
                isOriginInvalid = Origin.findByNumber(device.getOriginId()).isEmpty(),
                isProjectInvalid = Project.findByNumber(device.getProjectId()).isEmpty();
        if (useNonExistent) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Owner does not exist");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isSerialNumberExistent) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Serial number value of this device is already existed");
            error.setStatusCode(BAD_REQUEST);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isItemTypeInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Item type value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isRamInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Ram value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isStorageInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Storage value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isScreenInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Screen value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isPlatformInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Platform value of this device is non existent");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isStatusInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Status value of this device is invalid");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isOriginInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Origin value of this device is invalid");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (isProjectInvalid) {
            ErrorMessage error = new ErrorMessage();
            error.setMessage("Project value of this device is invalid");
            error.setStatusCode(NOT_FOUND);
            error.setServerTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            errors.add(error);
        }
        if (errors.size() > 0)
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(errors, NOT_ACCEPTABLE));

        deviceDetail.setName(device.getName().trim());
        deviceDetail.setStatus(Status.values()[device.getStatusId()]);
        deviceDetail.setSerialNumber(device.getSerialNumber().trim());
        deviceDetail.setInventoryNumber(device.getInventoryNumber().trim());
        deviceDetail.setProject(Project.values()[device.getProjectId()]);
        deviceDetail.setOrigin(Origin.values()[device.getOriginId()]);
        deviceDetail.setPlatformId(device.getPlatformId());
        deviceDetail.setRamId(device.getRamId());
        deviceDetail.setItemTypeId(device.getItemTypeId());
        deviceDetail.setStorageId(device.getStorageId());
        deviceDetail.setScreenId(device.getScreenId());
        deviceDetail.setComments(device.getComments());
        deviceDetail.setOwnerId(Integer.valueOf(device.getOwnerId()));
        deviceDetail.setUpdatedDate(new Date());
        _deviceRepository.save(deviceDetail);
        detailDeviceResponse.setUpdatedDevice(deviceDetail);
        return CompletableFuture.completedFuture(new ResponseEntity<Object>(detailDeviceResponse, OK));
    }

    @Override
    public void formatFilter(FilterDeviceDTO deviceFilterDTO) {
        if (deviceFilterDTO.getName() != null) deviceFilterDTO.setName(deviceFilterDTO.getName().trim().toLowerCase());

        if (deviceFilterDTO.getPlatformName() != null)
            deviceFilterDTO.setPlatformName(deviceFilterDTO.getPlatformName().trim().toLowerCase());

        if (deviceFilterDTO.getPlatformVersion() != null)
            deviceFilterDTO.setPlatformVersion(deviceFilterDTO.getPlatformVersion().trim().toLowerCase());

        if (deviceFilterDTO.getRam() != null) deviceFilterDTO.setRam(deviceFilterDTO.getRam().trim().toLowerCase());

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
    public CompletableFuture<ResponseEntity<Object>> deleteADevice(int deviceId) {
        DeleteDeviceResponse response = new DeleteDeviceResponse();

        if (_deviceRepository.findById(deviceId) == null) {
            response.setErrorMessage("Device is not existent");
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(response, NOT_FOUND));
        }

        _deviceRepository.deleteById((long) deviceId);
        response.setIsDeletionSuccessful(true);
        return CompletableFuture.completedFuture(new ResponseEntity<Object>(response, OK));
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
    public void downloadTemplate(HttpServletResponse response) throws IOException, InterruptedException, ExecutionException {
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
                        return CompletableFuture.completedFuture(new ResponseEntity<>("Sheet \"Devices\" is nonexistent", NOT_FOUND));
                    numberOfRows = DeviceExcelImporter.getNumberOfNonEmptyCells(sheet, 0);
                    if (numberOfRows == 0)
                        return CompletableFuture.completedFuture(new ResponseEntity<>("Sheet must be not empty", BAD_REQUEST));

                    for (; rowIndex < numberOfRows; rowIndex++) {
                        if (rowIndex == 0) continue;

                        Row currentRow = sheet.getRow(rowIndex);
                        String[] platformString = currentRow.getCell(DEVICE_PLATFORM).toString().split(",");
                        Device device;

                        String name = currentRow.getCell(DEVICE_NAME).toString().strip(), inventoryNumber = currentRow.getCell(DEVICE_INVENTORY_NUMBER).toString().strip(), serialNumber = currentRow.getCell(DEVICE_SERIAL_NUMBER).toString().strip(), comments = currentRow.getCell(DEVICE_COMMENTS).toString(), platformName = platformString[0].strip(), platfornmVersion = platformString[1].strip();

                        CompletableFuture<ItemType> itemType = _itemTypeService.findByName(currentRow.getCell(DEVICE_ITEM_TYPE).toString().strip());
                        CompletableFuture<Platform> platform = _platformService.findByNameAndVersion(platformName, platfornmVersion);
                        CompletableFuture<Ram> ram = _ramService.findBySize((int) currentRow.getCell(DEVICE_RAM).getNumericCellValue());
                        CompletableFuture<Screen> screen = _screenService.findBySize((int) currentRow.getCell(DEVICE_SCREEN).getNumericCellValue());
                        CompletableFuture<Storage> storage = _storageService.findBySize((int) currentRow.getCell(DEVICE_STORAGE).getNumericCellValue());
                        CompletableFuture<User> owner = _employeeService.findByUsername(currentRow.getCell(DEVICE_OWNER).toString().strip());

                        Status status = Status.valueOf(currentRow.getCell(DEVICE_STATUS).toString().strip());
                        Origin origin = Origin.valueOf(currentRow.getCell(DEVICE_ORIGIN).toString().strip());
                        Project project = Project.valueOf(currentRow.getCell(DEVICE_PROJECT).toString().strip());
                        Device existDevice = _deviceRepository.findBySerialNumber(serialNumber);

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
                        // Update
                        if (existDevice == null) {
                            device = Device.builder().name(name).status(status).ramId(ram.get().getId()).platformId(platform.get().getId()).screenId(screen.get().getId()).storageId(storage.get().getId()).ownerId(owner.get().getId()).origin(origin).project(project).comments(comments).itemTypeId(itemType.get().getId()).inventoryNumber(inventoryNumber).serialNumber(serialNumber).build();
                            device.setCreatedDate(new Date());
                            deviceList.add(device);
                            continue;
                        }
                        existDevice.setName(name);
                        existDevice.setStatus(status);
                        existDevice.setInventoryNumber(inventoryNumber);
                        existDevice.setProject(project);
                        existDevice.setOrigin(origin);
                        existDevice.setPlatformId(platform.get().getId());
                        existDevice.setRamId(ram.get().getId());
                        existDevice.setItemTypeId(itemType.get().getId());
                        existDevice.setStorageId(storage.get().getId());
                        existDevice.setScreenId(screen.get().getId());
                        existDevice.setComments(comments);
                        existDevice.setOwnerId(owner.get().getId());
                        existDevice.setUpdatedDate(new Date());
                        deviceList.add(existDevice);
                    }
                    workBook.close();
                    // Display list of error fields
                    if (!errors.isEmpty()) {
                        ImportError importError = new ImportError(errors);
                        return CompletableFuture.completedFuture(new ResponseEntity<Object>(importError, BAD_REQUEST));
                    }
                } catch (Exception e) {
                    // For duplicate elements of inventory number
                    throw new DataIntegrityViolationException(((DataIntegrityViolationException) e).getMostSpecificCause().getLocalizedMessage(), e);
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

    @Async()
    @Override
    public CompletableFuture<List<Device>> fetchFilteredDevice(FilterDeviceDTO deviceFilter, List<Device> devices) {
        if (deviceFilter.getName() != null)
            devices = devices.stream().filter(device -> device.getName().toLowerCase().equals(deviceFilter.getName())).collect(Collectors.toList());
        if (deviceFilter.getStatus() != null)
            devices = devices.stream().filter(device -> device.getStatus().name().equalsIgnoreCase(deviceFilter.getStatus())).collect(Collectors.toList());
        if (deviceFilter.getPlatformName() != null)
            devices = devices.stream().filter(device -> device.getPlatform().getName().toLowerCase().equals(deviceFilter.getPlatformName())).collect(Collectors.toList());
        if (deviceFilter.getPlatformVersion() != null)
            devices = devices.stream().filter(device -> device.getPlatform().getVersion().toLowerCase().equals(deviceFilter.getPlatformVersion())).collect(Collectors.toList());
        if (deviceFilter.getItemType() != null)
            devices = devices.stream().filter(device -> device.getItemType().getName().toLowerCase().equals(deviceFilter.getItemType())).collect(Collectors.toList());
        if (deviceFilter.getRam() != null)
            devices = devices.stream().filter(device -> device.getRam().getSize().toString().toLowerCase().equals(deviceFilter.getRam())).collect(Collectors.toList());
        if (deviceFilter.getScreen() != null)
            devices = devices.stream().filter(device -> device.getStorage().getSize().toString().toLowerCase().equals(deviceFilter.getScreen())).collect(Collectors.toList());
        if (deviceFilter.getStorage() != null)
            devices = devices.stream().filter(device -> device.getStorage().getSize().toString().toLowerCase().equals(deviceFilter.getStorage())).collect(Collectors.toList());
        if (deviceFilter.getOwner() != null)
            devices = devices.stream().filter(device -> device.getOwner().getUserName().toLowerCase().equals(deviceFilter.getOwner())).collect(Collectors.toList());
        if (deviceFilter.getOrigin() != null)
            devices = devices.stream().filter(device -> device.getOrigin().name().equalsIgnoreCase(deviceFilter.getOrigin())).collect(Collectors.toList());
        if (deviceFilter.getInventoryNumber() != null)
            devices = devices.stream().filter(device -> device.getInventoryNumber().toLowerCase().equals(deviceFilter.getInventoryNumber())).collect(Collectors.toList());
        if (deviceFilter.getSerialNumber() != null)
            devices = devices.stream().filter(device -> device.getSerialNumber().toLowerCase().equals(deviceFilter.getSerialNumber())).collect(Collectors.toList());
        if (deviceFilter.getProject() != null)
            devices = devices.stream().filter(device -> device.getProject().name().equalsIgnoreCase(deviceFilter.getProject())).collect(Collectors.toList());
        if (deviceFilter.getBookingDate() != null)
            devices = devices.stream().filter(request -> request.getBookingDate().after(deviceFilter.getBookingDate())).collect(Collectors.toList());
        if (deviceFilter.getReturnDate() != null)
            devices = devices.stream().filter(request -> request.getReturnDate().before(deviceFilter.getReturnDate())).collect(Collectors.toList());
        return CompletableFuture.completedFuture(devices);
    }

    @Async()
    @Override
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordDevices(int fieldColumn, String keyword, FilterDeviceDTO deviceFilter) throws InterruptedException, ExecutionException {
        if (keyword.trim().isBlank())
            return CompletableFuture.completedFuture(ResponseEntity.status(NOT_FOUND).body("Keyword must be non-null"));
        Set<String> keywordList = new HashSet<>();
        List<Device> devices = _deviceRepository.findAll();
        formatFilter(deviceFilter);
        devices = fetchFilteredDevice(deviceFilter, devices).get();
        // Fetch only one column
        switch (fieldColumn) {
            case DEVICE_NAME_COLUMN:
                keywordList = devices.stream().filter(device -> device.getName().contains(keyword)).map(device -> device.getName()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_PLATFORM_NAME_COLUMN:
                keywordList = devices.stream().filter(device -> device.getPlatform().getName().contains(keyword)).map(device -> device.getPlatform().getName()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_PLATFORM_VERSION_COLUMN:
                keywordList = devices.stream().filter(device -> device.getPlatform().getVersion().contains(keyword)).map(device -> device.getPlatform().getVersion()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_RAM_COLUMN:
                keywordList = devices.stream().filter(device -> device.getRam().getSize().toString().contains(keyword)).map(device -> device.getRam().getSize().toString()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_SCREEN_COLUMN:
                keywordList = devices.stream().filter(device -> device.getScreen().getSize().toString().contains(keyword)).map(device -> device.getScreen().getSize().toString()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_STORAGE_COLUMN:
                keywordList = devices.stream().filter(device -> device.getStorage().getSize().toString().contains(keyword)).map(device -> device.getStorage().getSize().toString()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_OWNER_COLUMN:
                keywordList = devices.stream().filter(device -> device.getOwner().getUserName().contains(keyword)).map(device -> device.getOwner().getUserName()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_INVENTORY_NUMBER_COLUMN:
                keywordList = devices.stream().filter(device -> device.getInventoryNumber().contains(keyword)).map(device -> device.getInventoryNumber()).limit(20).collect(Collectors.toSet());
                break;
            case DEVICE_SERIAL_NUMBER_COLUMN:
                keywordList = devices.stream().filter(device -> device.getSerialNumber().contains(keyword)).map(device -> device.getSerialNumber()).limit(20).collect(Collectors.toSet());
                break;
        }
        KeywordSuggestionResponse response = new KeywordSuggestionResponse();
        response.setKeywordList(keywordList);
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, OK));
    }

    @Async()
    @Override
    public CompletableFuture<DropdownValuesResponse> getDropDownValues() throws InterruptedException, ExecutionException {
        CompletableFuture<List<ItemTypeList>> itemTypeList = _itemTypeService.fetchItemTypes();
        CompletableFuture<List<RamList>> ramList = _ramService.fetchRams();
        CompletableFuture<List<PlatformList>> platformList = _platformService.fetchPlatform();
        CompletableFuture<List<ScreenList>> screenList = _screenService.fetchScreen();
        CompletableFuture<List<StorageList>> storageList = _storageService.fetchStorage();
        List<StatusList> statusList = getStatusList().get();
        List<ProjectList> projectList = getProjectList().get();
        List<OriginList> originList = getOriginList().get();
        DropdownValuesResponse response = new DropdownValuesResponse();
        response.setItemTypeList(itemTypeList.get());
        response.setRamList(ramList.get());
        response.setPlatformList(platformList.get());
        response.setScreenList(screenList.get());
        response.setStorageList(storageList.get());
        response.setStatusList(statusList);
        response.setProjectList(projectList);
        response.setOriginList(originList);
        return CompletableFuture.completedFuture(response);
    }

    @Async()
    public CompletableFuture<List<StatusList>> getStatusList() {
        Status[] statusCode = Status.values();
        List<StatusList> statusList = new ArrayList<StatusList>();
        for (int i = 0; i < statusCode.length; i++) {
            StatusList item = new StatusList(i, statusCode[i].toString());
            statusList.add(item);
        }
        return CompletableFuture.completedFuture(statusList);
    }

    @Async()
    public CompletableFuture<List<ProjectList>> getProjectList() {
        Project[] projectCode = Project.values();
        List<ProjectList> projectList = new ArrayList<ProjectList>();
        for (int i = 0; i < projectCode.length; i++) {
            ProjectList item = new ProjectList(i, projectCode[i].toString());
            projectList.add(item);
        }
        return CompletableFuture.completedFuture(projectList);
    }

    @Async()
    public CompletableFuture<List<OriginList>> getOriginList() {
        Origin[] originCode = Origin.values();
        List<OriginList> originList = new ArrayList<OriginList>();
        for (int i = 0; i < originCode.length; i++) {
            OriginList item = new OriginList(i, originCode[i].toString());
            originList.add(item);
        }
        return CompletableFuture.completedFuture(originList);
    }
}
