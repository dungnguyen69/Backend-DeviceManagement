package com.fullstack.Backend.dto.device;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DropDownListsDTO {
	String[] itemTypeList;
	String[] statusList;
	String[] platformList;
	String[] ramList;
	String[] screenList;
	String[] storageList;
	String[] projectList;
	String[] originList;
}
