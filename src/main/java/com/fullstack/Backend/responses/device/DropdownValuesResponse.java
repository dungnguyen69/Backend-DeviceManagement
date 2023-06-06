package com.fullstack.Backend.responses.device;

import java.util.List;

import com.fullstack.Backend.utils.dropdowns.ItemTypeList;
import com.fullstack.Backend.utils.dropdowns.OriginList;
import com.fullstack.Backend.utils.dropdowns.PlatformList;
import com.fullstack.Backend.utils.dropdowns.ProjectList;
import com.fullstack.Backend.utils.dropdowns.RamList;
import com.fullstack.Backend.utils.dropdowns.ScreenList;
import com.fullstack.Backend.utils.dropdowns.StatusList;
import com.fullstack.Backend.utils.dropdowns.StorageList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DropdownValuesResponse {
	List<StatusList> statusList;
	List<ItemTypeList> itemTypeList;
	List<OriginList> originList;
	List<PlatformList> platformList;
	List<ScreenList> screenList;
	List<ProjectList> projectList;
	List<StorageList> storageList;
	List<RamList> ramList;
}
