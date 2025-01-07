package com.sky.controller.admin;

import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeePassWordDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "用户管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("用户登录：{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .role(employee.getRole())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 学生分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/pageStudent")
    @ApiOperation("学生分页查询")
    @Transactional
    public Result<PageResult> pageStudent(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        PageResult pageResult = employeeService.pageStudentQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 维修员分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/pageMaintenance")
    @ApiOperation("维修员分页查询")
    @Transactional
    public Result<PageResult> pageMaintenance(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        PageResult pageResult = employeeService.pageMaintenanceQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用账户
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用账户")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 新增用户
     * @param employeeDTO
     */
    @PostMapping
    @ApiOperation("新增用户")
    public Result<String> save(EmployeeDTO employeeDTO) {
        employeeService.insertEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 修改用户
     * @param employeePassWordDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改用户")
    public Result<String> update(@RequestBody EmployeePassWordDTO employeePassWordDTO){
        employeeService.update(employeePassWordDTO);
        return Result.success();
    }

    /**
     * 删除用户
     * @param name
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除用户")
    public Result<String> deleteByName(String name){
        log.info("删除用户：{}", name);
        employeeService.deleteByName(name);
        return Result.success();
    }
}
