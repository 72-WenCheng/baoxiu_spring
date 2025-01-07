package com.sky.controller.admin;

import com.sky.dto.MaintenanceDTO;
import com.sky.result.Result;
import com.sky.service.MaintenanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/maintenance")
@Slf4j
@Api(tags = "维修管理")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * 新增维修
     * @param maintenanceDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增维修")
    @Transactional
    public Result<String> save(@RequestBody MaintenanceDTO maintenanceDTO){
        log.info("新增维修：{}", maintenanceDTO);
        maintenanceService.save(maintenanceDTO);
        return Result.success();
    }
}
