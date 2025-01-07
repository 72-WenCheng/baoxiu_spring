package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.IdentityConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DispatchDTO;
import com.sky.dto.DispatchPageQueryDTO;
import com.sky.entity.Dispatch;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DispatchMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.RepairMapper;
import com.sky.result.PageResult;
import com.sky.service.DispatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RepairMapper repairMapper;


    /**
     * 新增派修
     * @param dispatchDTO
     */
    public void saveWith(DispatchDTO dispatchDTO) {
        Dispatch dispatch = new Dispatch();

        //获取employee
        Employee employee = employeeMapper.getSelect(dispatchDTO.getName());

        //判断有无该维修员
        if (!(dispatchDTO.getName().equals(employee.getName())) || !(employee.getRole().equals(IdentityConstant.Maintainer))){
            throw new DeletionNotAllowedException(MessageConstant.The_Repairman_Does_Not_Exist);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //维修员账号被封禁
            throw new AccountLockedException(MessageConstant.USER_NOT_LOGIN);
        }

        //派修状态设置默认为未处理
        dispatchDTO.setOrderStatus(StatusConstant.NotProcessed);

        //将报修的状态更改为已派修
        repairMapper.updateRepairStatus(dispatchDTO.getId());

        //属性拷贝
        BeanUtils.copyProperties(dispatchDTO, dispatch);

        //前端崩了，最后再次判断是否已经给这个报修单派维修人员过去了
        Integer num = dispatchMapper.ByIdNum(dispatch.getId());
        if (num != null){
            throw new DeletionNotAllowedException(MessageConstant.Repair_Order_To_Be_Repaired);
        }else {
            try {
                //没有派修单的情况
                dispatchMapper.insert(dispatch);
            }catch (Exception e){
                throw new DeletionNotAllowedException(MessageConstant.No_Repair_Order_For_This_Faction);
            }
        }

        //数据已经sql写入，所以update处理，将外键写入到数据库
        Long ids = dispatchDTO.getId();
        dispatchMapper.updateId(ids);
    }

    /**
     * 派修分页查询
     * @param dispatchPageQueryDTO
     * @return
     */
    public PageResult pageQueryDispatch(DispatchPageQueryDTO dispatchPageQueryDTO) {
        //按派修的维修员信息为准
        //查询的时候做一些不存在的报错信息
        //这里是根据用户名字查询
        String num = dispatchMapper.ByName(dispatchPageQueryDTO.getName());
        if (num == null || num.equals("")) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        PageHelper.startPage(dispatchPageQueryDTO.getPage(),dispatchPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Dispatch> page = dispatchMapper.pageQueryDispatch(dispatchPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改报修
     * @param dispatchDTO
     */
    public void updateDispatch(DispatchDTO dispatchDTO) {
        //修改的时候，其余字段不修改就默认原来的值
        //这里也是防止前端崩溃，乱传数据
        Dispatch dispatch = dispatchMapper.ByIdDispatch(dispatchDTO.getId());

        //维修员逻辑处理
       try {
           String num = employeeMapper.ByName(dispatchDTO.getName());
           if (num == null || num.equals("")){
               throw new DeletionNotAllowedException(MessageConstant.The_Repairman_Does_Not_Exist);
           }
       }catch (Exception e){
           throw new DeletionNotAllowedException(MessageConstant.The_Repairman_Does_Not_Exist);
       }finally {
           Employee employee = employeeMapper.getSelect(dispatchDTO.getName());
          try {
              if (!(employee.getRole().equals(IdentityConstant.Maintainer))){
                  throw new DeletionNotAllowedException(MessageConstant.The_Repairman_Does_Not_Exist);
              }else if(employee.getStatus() == StatusConstant.DISABLE){
                  //维修员账号被封禁
                  throw new AccountLockedException(MessageConstant.USER_NOT_LOGIN);
              }
          }catch (Exception e){
              throw new DeletionNotAllowedException(MessageConstant.The_Repairman_Does_Not_Exist);
          }
       }

       //判断派修单是否结束
        Integer num = dispatchMapper.ByIdOrderStatus(dispatchDTO.getId());
        if (num == StatusConstant.Processed){
            throw new DeletionNotAllowedException(MessageConstant.The_Repair_Order_Has_Been_Processed);
        }

        dispatchDTO.setDispatchTime(dispatch.getDispatchTime());
        dispatchMapper.UpdateDispatch(dispatchDTO);
    }

    /**
     * 删除派修
     * @param id
     */
    public void deleteById(Long id) {
        //查询派修状态是否已经结束
        Integer num = dispatchMapper.ByIdOrderStatus(id);
        if (!(num == StatusConstant.Processed)){
            throw new DeletionNotAllowedException(MessageConstant.Your_Ticket_Has_Not_Yet_Been_Processed);
        }
        dispatchMapper.deleteById(id);
    }
}
