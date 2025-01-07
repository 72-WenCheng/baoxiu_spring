package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeePassWordDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 学生分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageStudentQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 维修员分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageMaintenanceQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用账户
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 新增用户
     * @param employeeDTO
     */
    void insertEmployee(EmployeeDTO employeeDTO);

    /**
     * 修改用户
     * @param employeePassWordDTO
     */
    void update(EmployeePassWordDTO employeePassWordDTO);

    /**
     * 根据名字删除用户
     * @param name
     */
    void deleteByName(String name);
}
