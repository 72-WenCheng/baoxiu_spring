package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class RepairDTO implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private String description;

    private String image;

    private String louNum;

    private String fangNum;

    private String repairStatus;

    private Long sort;
}
