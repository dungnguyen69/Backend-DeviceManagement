package com.fullstack.Backend.responses.device;

import com.fullstack.Backend.dto.device.KeepingDeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeepingDeviceResponse {
    private List<KeepingDeviceDTO> devicesList;
    private List<String> statusList;
    private List<String> originList;
    private List<String> projectList;
    private List<String> itemTypeList;
    private List<String> keeperNumberOptions;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
