package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class MaintenanceDTO implements Serializable {

    private Long id;

    private Long dispatchId;

    private String faultAnalysis;

    private String repairProcess;

    private String repairResult;

}
