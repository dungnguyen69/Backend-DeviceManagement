package com.fullstack.Backend;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeviceServiceTest {
//    private static final String END_POINT_PATH = "/api/devices";
//    private MockMvc mockMvc;
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    ObjectWriter objectWriter = objectMapper.writer();
//
//    @Autowired
//    private DeviceService deviceService;
//
//    /*@InjectMocks
//    private DeviceController deviceController;*/
//
//    /*@Before
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(deviceController).buildz();
//    }*/
//
//    @Test
//    public void showDevicesWithPaging_success() throws Exception {
//        String jsonResult = """
//                    {
//                    "devicesList": [
//                        {
//                            "Id": 38,
//                            "DeviceName": "Air pal",
//                            "Status": "OCCUPIED",
//                            "ItemType": "Laptop",
//                            "PlatformName": "N/A",
//                            "PlatformVersion": "N/A",
//                            "RamSize": "8 GB",
//                            "ScreenSize": "N/A",
//                            "StorageSize": "N/A",
//                            "InventoryNumber": "ABC1b4",
//                            "SerialNumber": "t123",
//                            "Comments": null,
//                            "Project": "TELSA",
//                            "Origin": "Internal",
//                            "Owner": "valiance",
//                            "Keeper": "valiance",
//                            "KeeperNumber": 0,
//                            "BookingDate": null,
//                            "ReturnDate": null,
//                            "CreatedDate": 1690212640280,
//                            "UpdatedDate": 1690484592856
//                        },
//                        {
//                            "Id": 37,
//                            "DeviceName": "Samsung XR",
//                            "Status": "VACANT",
//                            "ItemType": "Mobile",
//                            "PlatformName": "Android",
//                            "PlatformVersion": "11.1",
//                            "RamSize": "4 GB",
//                            "ScreenSize": "13 inch",
//                            "StorageSize": "512 GB",
//                            "InventoryNumber": "96548935",
//                            "SerialNumber": "76692069A",
//                            "Comments": "",
//                            "Project": "TELSA",
//                            "Origin": "Internal",
//                            "Owner": "valiance",
//                            "Keeper": "valiance",
//                            "KeeperNumber": 0,
//                            "BookingDate": null,
//                            "ReturnDate": null,
//                            "CreatedDate": 1689390185324,
//                            "UpdatedDate": 1689390210454
//                        },
//                        {
//                            "Id": 36,
//                            "DeviceName": "Mac",
//                            "Status": "OCCUPIED",
//                            "ItemType": "N/A",
//                            "PlatformName": "N/A",
//                            "PlatformVersion": "N/A",
//                            "RamSize": "N/A",
//                            "ScreenSize": "13 inch",
//                            "StorageSize": "512 GB",
//                            "InventoryNumber": "111111",
//                            "SerialNumber": "54318",
//                            "Comments": "",
//                            "Project": "TELSA",
//                            "Origin": "Internal",
//                            "Owner": "ncdung",
//                            "Keeper": "valiance",
//                            "KeeperNumber": 2,
//                            "BookingDate": 1689354000000,
//                            "ReturnDate": 1689958800000,
//                            "CreatedDate": 1689140502092,
//                            "UpdatedDate": 1689140670664
//                        }
//                    ],
//                    "statusList": [
//                        "OCCUPIED",
//                        "VACANT",
//                        "UNAVAILABLE",
//                        "BROKEN"
//                    ],
//                    "originList": [
//                        "Internal",
//                        "External"
//                    ],
//                    "projectList": [
//                        "TELSA"
//                    ],
//                    "itemTypeList": [
//                        "Laptop",
//                        "Mobile",
//                        "N/A"
//                    ],
//                    "keeperNumberOptions": [
//                        "LESS THAN 3",
//                        "EQUAL TO 3"
//                    ],
//                    "pageNo": 1,
//                    "pageSize": 3,
//                    "totalElements": 8,
//                    "totalPages": 3
//                }""";
//        FilterDeviceDTO deviceFilter = new FilterDeviceDTO();
//        CompletableFuture<ResponseEntity<Object>> result = deviceService.showDevicesWithPaging(1, 3, "id", "desc", deviceFilter);
//        Object responseBody = result.get().getBody();
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(responseBody);
//        System.out.println(json);
//        assertNotNull(result);
//        assertEquals(jsonResult.replaceAll("\\s", ""), json.replaceAll("\\s", ""));
//    }
}
