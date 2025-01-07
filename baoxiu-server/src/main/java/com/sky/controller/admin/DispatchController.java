package com.sky.controller.admin;

import com.github.pagehelper.PageHelper;
import com.sky.dto.DispatchDTO;
import com.sky.dto.DispatchPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dispatch")
@Slf4j
@Api(tags = "派修管理")
public class DispatchController {

    @Autowired
    private DispatchService dispatchService;

    /**
     * 新增派修
     * @param dispatchDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增派修")
    public Result<String> save(@RequestBody DispatchDTO dispatchDTO) {
        log.info("新增派修：{}", dispatchDTO);
        dispatchService.saveWith(dispatchDTO);
        return Result.success();
    }

    /**
     * 派修分页查询
     * @param dispatchPageQueryDTO
     * @return
     */
    @GetMapping("/pages")
    @ApiOperation("派修分页查询")
    @Transactional
    public Result<PageResult> pages(DispatchPageQueryDTO dispatchPageQueryDTO) {
        PageHelper.startPage(dispatchPageQueryDTO.getPage(),dispatchPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        PageResult pageResult = dispatchService.pageQueryDispatch(dispatchPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改派修
     * @param dispatchDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改报修")
    public Result<String> update(@RequestBody DispatchDTO dispatchDTO) {
        dispatchService.updateDispatch(dispatchDTO);
        return Result.success();
    }

    /**
     * 删除派修
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除派修")
    public Result<String> deleteById(Long id){
        log.info("删除派修：{}", id);
        dispatchService.deleteById(id);
        return Result.success();
    }
}
