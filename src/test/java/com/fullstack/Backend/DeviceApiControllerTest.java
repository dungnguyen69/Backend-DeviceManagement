package com.fullstack.Backend;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


/*https://thepracticaldeveloper.com/guide-spring-boot-controller-tests/#strategy-1-spring-mockmvc-example-in-standalone-mode*/
@ExtendWith(MockitoExtension.class)
public class DeviceApiControllerTest {
//    private static final String END_POINT = "/api/devices";
//
//    private MockMvc mockMvc;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//    private final MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
//
//    @Mock
//    private DeviceService deviceService;
//
//    @InjectMocks
//    private DeviceController deviceController;
//
//    private final DeviceMapper deviceMapper = new DeviceMapperImp();
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(deviceController).build();
//    }
//
//    @Test
//    public void testListShouldReturn200() throws Exception {
//        DeviceDTO mockDevice1 = DeviceDTO.builder().Id(1).DeviceName("Air pal").Status("OCCUPIED")
//                .RamSize("8 GB").PlatformName("N/A").PlatformVersion("N/A")
//                .ItemType("Laptop").ScreenSize("N/A").StorageSize("N/A")
//                .InventoryNumber("ABC1b4").SerialNumber("t123").Project("TELSA")
//                .Origin("Internal").Owner("valiance").Keeper("valiance")
//                .keeperNumber(0).BookingDate(null).ReturnDate(null)
//                .CreatedDate(null).UpdatedDate(null).build();
//        DeviceDTO mockDevice2 = DeviceDTO.builder().Id(2).DeviceName("Air pal").Status("OCCUPIED")
//                .RamSize("8 GB").PlatformName("N/A").PlatformVersion("N/A")
//                .ItemType("Laptop").ScreenSize("N/A").StorageSize("N/A")
//                .InventoryNumber("ABC1b4").SerialNumber("t123").Project("TELSA")
//                .Origin("Internal").Owner("valiance").Keeper("valiance")
//                .keeperNumber(0).BookingDate(null).ReturnDate(null)
//                .CreatedDate(null).UpdatedDate(null).build();
//        List<DeviceDTO> deviceList = List.of(mockDevice1, mockDevice2);
//        List<String> statusList = deviceList.stream().map(DeviceDTO::getStatus).distinct().collect(Collectors.toList());
//        List<String> originList = deviceList.stream().map(DeviceDTO::getOrigin).distinct().collect(Collectors.toList());
//        List<String> projectList = deviceList.stream().map(DeviceDTO::getProject).distinct().collect(Collectors.toList());
//        List<String> itemTypeList = deviceList.stream().map(DeviceDTO::getItemType).distinct().collect(Collectors.toList());
//        List<String> keeperNumberOptions = List.of(new String[]{"LESS THAN 3", "EQUAL TO 3"});
//        DeviceInWarehouseResponse deviceResponse = new DeviceInWarehouseResponse(
//                deviceList, statusList, originList, projectList, itemTypeList, keeperNumberOptions,
//                1, 2, 2, 1);
//
//        Mockito.when(deviceService
//                        .showDevicesWithPaging(eq(1), eq(2), eq("id"), eq("desc"),
//                                Mockito.any(FilterDeviceDTO.class)))
//                .thenReturn(CompletableFuture.completedFuture(new ResponseEntity<>(deviceResponse, OK)));
//
//        String expected = ow.writeValueAsString(new ResponseEntity<>(deviceResponse, OK).getBody());
//        String requestURI = END_POINT + "/warehouse?pageNo=1&pageSize=2";
//        MvcResult mvcResult = this.mockMvc.perform(get(requestURI)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .characterEncoding("utf-8"))
//                .andDo(print())
//                .andReturn();
//
//        verify(deviceService, timeout(2000)
//                        .times(1))
//                .showDevicesWithPaging(eq(1), eq(2), eq("id"), eq("desc"),
//                        Mockito.any(FilterDeviceDTO.class));
//
//        JsonNode jsonNode = objectMapper.readTree(ow.writeValueAsString(mvcResult.getAsyncResult()));
//        String actual = jsonNode.get("body").toPrettyString();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testAddShouldReturn400BadRequest() throws Exception {
//        AddDeviceDTO newDevice = AddDeviceDTO.builder().deviceName("Air pod").statusId(2)
//                .platformId(1).itemTypeId(21).ramId(3).screenId(1).storageId(1)
//                .owner("valiance").inventoryNumber("ABC1b4").serialNumber("12345XT")
//                .originId(0).projectId(1).comments(null)
//                .build();
//
//        when(deviceService.addDevice(refEq(newDevice)))
//                .thenReturn(CompletableFuture.completedFuture(new ResponseEntity<>(Mockito.any(ArrayList.class), BAD_REQUEST)));
//        String requestBody = ow.writeValueAsString(newDevice);
//        MvcResult mvcResult = mockMvc.perform(post(END_POINT + "/warehouse")
//                        .contentType(MEDIA_TYPE_JSON_UTF8)
//                        .content(requestBody)
//                        .accept(MEDIA_TYPE_JSON_UTF8)
//                        .characterEncoding("utf-8"))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        verify(deviceService, Mockito.timeout(2000).times(1)).addDevice(refEq(newDevice));
//        JsonNode jsonNode = objectMapper.readTree(ow.writeValueAsString(mvcResult.getAsyncResult()));
//        String actual = jsonNode.get("statusCodeValue").toPrettyString();
//        assertEquals("400", actual);
//    }
//
//    @Test
//    public void testAddShouldReturn200Ok() throws Exception {
//        AddDeviceDTO newDevice1 = AddDeviceDTO.builder().deviceName("Air pod").statusId(2)
//                .platformId(1).itemTypeId(2).ramId(3).screenId(1).storageId(1)
//                .owner("valiance").inventoryNumber("ABC1b4").serialNumber("12345XT")
//                .originId(0).projectId(1).comments(null)
//                .build();
//        AddDeviceDTO newDevice2 = AddDeviceDTO.builder().deviceName("Air pod").statusId(2)
//                .platformId(11).itemTypeId(2).ramId(3).screenId(1).storageId(1)
//                .owner("valiance").inventoryNumber("ABC1b4").serialNumber("12345XT")
//                .originId(0).projectId(1).comments(null)
//                .build();
//        Device device = deviceMapper.addDeviceDtoToDevice(newDevice1);
//        AddDeviceResponse addDeviceResponse = new AddDeviceResponse(device, true);
//        when(deviceService.addDevice(refEq(newDevice1))).thenReturn(CompletableFuture.completedFuture(ResponseEntity.ok(addDeviceResponse)));
//        String requestBody = ow.writeValueAsString(newDevice1);
//        MvcResult mvcResult = mockMvc.perform(post(END_POINT + "/warehouse")
//                        .contentType(MEDIA_TYPE_JSON_UTF8)
//                        .content(requestBody)
//                        .accept(MEDIA_TYPE_JSON_UTF8)
//                        .characterEncoding("utf-8"))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//        verify(deviceService, Mockito.times(1)).addDevice(refEq(newDevice1));
//        JsonNode jsonNode = objectMapper.readTree(ow.writeValueAsString(mvcResult.getAsyncResult(1000)));
//        int actual = Integer.parseInt(jsonNode.get("statusCodeValue").toPrettyString());
//        assertEquals(200, actual);
//    }
}