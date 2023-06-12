package com.fullstack.Backend.responses.device;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class KeywordSuggestionResponse {
	Set<String> keywordList;
}
