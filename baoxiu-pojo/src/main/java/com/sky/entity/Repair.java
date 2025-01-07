package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Repair implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private String description;

    private String image;

    private String louNum;

    private String fangNum;

    private Integer repairStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Long repairNum;

    private Long sort;
}
