package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class RepairPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    private Long repairNum;

    private String repairStatus;

}
