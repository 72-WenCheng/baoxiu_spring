package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance implements Serializable {

    private Long id;

    private Long dispatchId;

    private String faultAnalysis;

    private String repairProcess;

    private String repairResult;
}
