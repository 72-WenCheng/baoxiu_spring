package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.RepairDTO;
import com.sky.dto.RepairPageQueryDTO;
import com.sky.entity.Repair;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DispatchMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.RepairMapper;
import com.sky.result.PageResult;
import com.sky.service.RepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@Slf4j
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DispatchMapper dispatchMapper;

    /**
     * 新增报修
     * @param repairDTO
     */
    public void sava(RepairDTO repairDTO) {
        Repair repair = new Repair();

        //处理各种异常情况
        //获取当前登录用户id
        Long id = BaseContext.getCurrentId();
        String num = employeeMapper.ByIdEmployee(id);
        if (num == null || "".equals(num)) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //拷贝用户名和手机号，自动填充
        repairDTO.setName(repairMapper.ByIdNames(id));
        repairDTO.setPhone(repairMapper.ByIdPhones(id));

        //对象属性拷贝
        // TODO 排序字段自动填充
        BeanUtils.copyProperties(repairDTO, repair);

        //状态默认为未提交
        repair.setRepairStatus(StatusConstant.NotSubmitted);

        //报修单号随机数自动填充
        Random rand = new Random();
        Long nums = (long) ((int)rand.nextInt(9000) + 1000);
        repair.setRepairNum(nums);

        //判断报修状态是否维修结束
        try {
            //假设没有报修，会抛出空指针异常
            repairMapper.ByIdStatus(id);
        }catch (Exception e) {
            throw new DeletionNotAllowedException(MessageConstant.Reported_For_Repair_Yet);
        }finally {
            Integer count = repairMapper.ByIdStatus(id);
            if (count == null || count == StatusConstant.TheRepairIsOver) {
                repairMapper.insert(repair);
            }else {
                throw new DeletionNotAllowedException(MessageConstant.Your_Ticket_Has_Not_Yet_Been_Processed);
            }
        }
    }

    /**
     * 报修分页查询
     * @param repairPageQueryDTO
     * @return
     */
    public PageResult pageQuery(RepairPageQueryDTO repairPageQueryDTO) {
        //按报修用户名信息为准，假设用户名改变了，但是之前的报修单没有删除，还保留原有的名字
        //查询的时候做一些不存在的报错信息
        //这里是根据用户名字查询
        String num = repairMapper.ByName(repairPageQueryDTO.getName());
        if (num == null || num.equals("")) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        PageHelper.startPage(repairPageQueryDTO.getPage(),repairPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Repair> page = repairMapper.pageQuery(repairPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改报修
     * @param repairDTO
     */
    public void update(RepairDTO repairDTO) {
        Repair repair = repairMapper.ByIdRepair(repairDTO.getId());
        //防止前端崩溃，传了脏数据，报修单一旦生成，名字信息就不能修改，防止关联不上
        String name = repairMapper.ByIdName(repairDTO.getId());
        if (!(repair.getName().equals(name)) && !(repairDTO.getName().equals(name))){
            throw new DeletionNotAllowedException(MessageConstant.The_Name_Cannot_Be_Changed);
        }

        //判断是否提交了报修
        //提交之前可以修改其余信息
        Integer num = repairMapper.ByIdStatus(repairDTO.getId());
        if (!(num == StatusConstant.NotSubmitted)){
            throw new DeletionNotAllowedException(MessageConstant.Your_Repair_Order_Has_Been_Submitted);
        }

        //属性拷贝
        BeanUtils.copyProperties(repairDTO,repair);
        repairMapper.update(repair);
    }

    /**
     * 报修删除
     * @param id
     */
    public void deleteById(Long id) {
        //查看派修状态是否结束
        Integer num = dispatchMapper.ByIdOrderStatus(id);
        if (!(num == StatusConstant.Processed)){
            throw new DeletionNotAllowedException(MessageConstant.Your_Ticket_Has_Not_Yet_Been_Processed);
        }
        repairMapper.deleteById(id);
    }

}
