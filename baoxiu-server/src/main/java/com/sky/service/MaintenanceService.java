package com.sky.service;

import com.sky.dto.MaintenanceDTO;

public interface MaintenanceService {

    /**
     * 新增维修
     * @param maintenanceDTO
     */
    void save(MaintenanceDTO maintenanceDTO);

}
