package com.sky.controller.admin;

import com.sky.dto.RepairDTO;
import com.sky.dto.RepairPageQueryDTO;
import com.sky.mapper.RepairMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.RepairService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报修管理
 */
@RestController
@RequestMapping("/admin/repair")
@Slf4j
@Api(tags = "报修管理")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @Autowired
    private RepairMapper repairMapper;

    /**
     * 新增报修
     * @param repairDTO
     * @return
     */
    @PostMapping
    @ApiOperation("保存报修")
    public Result<String> save(@RequestBody RepairDTO repairDTO){
        log.info("保存报修：{}", repairDTO);
        repairService.sava(repairDTO);
        return Result.success();
    }

    /**
     * 报修分页查询
     * @param repairPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("报修分页查询")
    public Result<PageResult> page(RepairPageQueryDTO repairPageQueryDTO){
        log.info("报修分页查询：{}", repairPageQueryDTO);
        PageResult pageResult = repairService.pageQuery(repairPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改报修
     * @param repairDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改报修")
    public Result<String> update(@RequestBody RepairDTO repairDTO){
        repairService.update(repairDTO);
        return Result.success();
    }

    /**
     * 删除报修
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除报修")
    public Result<String> deleteById(Long id){
        log.info("删除报修：{}", id);
        repairService.deleteById(id);
        return Result.success();
    }

    /**
     * 提交报修
     */
    @PutMapping("/id/{id}")
    @ApiOperation("提交报修")
    public Result<String> updateCommit(@PathVariable Long id){
        repairMapper.updateCommit(id);
        return Result.success();
    }

}
