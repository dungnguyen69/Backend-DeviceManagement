package com.fullstack.Backend.dto.keeper_order;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class KeeperOrderAddDTO {
    private int deviceId;
    private int keeperId;
    private int keeperNo;
    private boolean isReturned;
    private Date bookingDate;
    private Date dueDate;
    private Date createdDate;
}
