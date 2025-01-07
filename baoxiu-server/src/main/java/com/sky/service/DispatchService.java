package com.sky.service;

import com.sky.dto.DispatchDTO;
import com.sky.dto.DispatchPageQueryDTO;
import com.sky.result.PageResult;

public interface DispatchService {

    /**
     * 新增派修
     * @param dispatchDTO
     */
    void saveWith(DispatchDTO dispatchDTO);

    /**
     * 派修分页查询
     * @param dispatchPageQueryDTO
     * @return
     */
    PageResult pageQueryDispatch(DispatchPageQueryDTO dispatchPageQueryDTO);

    /**
     * 修改报修
     * @param dispatchDTO
     */
    void updateDispatch(DispatchDTO dispatchDTO);

    /**
     * 删除派修
     * @param id
     */
    void deleteById(Long id);
}
