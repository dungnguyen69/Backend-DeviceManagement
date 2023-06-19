package com.fullstack.Backend.responses.users;

import com.fullstack.Backend.dto.users.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private List<UserDTO> usersList;
    private List<String> projectList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}