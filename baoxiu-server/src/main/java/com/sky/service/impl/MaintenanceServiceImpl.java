package com.sky.service.impl;

import com.sky.dto.MaintenanceDTO;
import com.sky.entity.Maintenance;
import com.sky.mapper.DispatchMapper;
import com.sky.mapper.MaintenanceMapper;
import com.sky.mapper.RepairMapper;
import com.sky.service.MaintenanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private RepairMapper repairMapper;


    /**
     * 新增维修
     * @param maintenanceDTO
     */
    public void save(MaintenanceDTO maintenanceDTO) {

        Maintenance maintenance = new Maintenance();

        //先进行对象属性拷贝，拿到主键自增的id值
        BeanUtils.copyProperties(maintenanceDTO, maintenance);
        maintenanceMapper.insertMaintenance(maintenance);


        //赋值给外键
        maintenance.setDispatchId(maintenance.getId());

        //接收
        Long ids = maintenance.getDispatchId();

        //业务逻辑
        //设置状态
        //派修单状态为已处理，报修单状态为维修结束
        dispatchMapper.updateMaintenanceStatusNoe(maintenance.getId());
        repairMapper.updateMaintenanceStatusTwo(ids);

        //数据已经sql写入，所以update处理，将外键写入到数据库
        maintenanceMapper.updateId(ids);

    }
}
