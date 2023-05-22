package com.fullstack.Backend.responses;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDeviceResponse {
	Set<String> keywordList;
}
