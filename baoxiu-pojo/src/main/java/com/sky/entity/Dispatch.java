package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dispatch implements Serializable {

    private Long id;

    private Long repairId;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dispatchTime;

    private Integer orderStatus;

    private Long repairNum;
}
