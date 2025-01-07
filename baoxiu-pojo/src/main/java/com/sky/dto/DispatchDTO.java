package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class DispatchDTO implements Serializable {

    private Long id;

    private Long repairId;

    private String name;

    //不进行时间自动填充，改用Date
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dispatchTime;

    private Integer orderStatus;

}
