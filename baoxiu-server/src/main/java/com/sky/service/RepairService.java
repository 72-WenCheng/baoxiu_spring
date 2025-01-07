package com.sky.service;

import com.sky.dto.RepairDTO;
import com.sky.dto.RepairPageQueryDTO;
import com.sky.result.PageResult;

public interface RepairService {

    /**
     * 保存报修
     * @param repairDTO
     */
    void sava(RepairDTO repairDTO);

    /**
     * 报修分页查询
     * @param repairPageQueryDTO
     * @return
     */
    PageResult pageQuery(RepairPageQueryDTO repairPageQueryDTO);

    /**
     * 修改报修
     * @param repairDTO
     */
    void update(RepairDTO repairDTO);

    /**
     * 删除报修
     * @param id
     */
    void deleteById(Long id);

}
