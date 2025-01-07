package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.*;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.EmployeePassWordDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.RepairMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RepairMapper repairMapper;

    /**
     * 用户登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //拿到前端的值用于逻辑校验
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        String role = employeeLoginDTO.getRole();

        //根据账号查询返回用户实体
        Employee employee = employeeMapper.getByUsername(username);

        //处理各种异常

        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被禁用
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        if (!role.equals(employee.getRole())){
            //防止前端崩溃，传了不符合角色的字段名字
            //身份校验失败
            throw new AccountNotFoundException(MessageConstant.Identity_Verification_Failed);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 学生分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageStudentQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //查询的时候做一些不存在的报错信息
        //这里是根据用户名字查询
        String num = employeeMapper.ByName(employeePageQueryDTO.getName());
        if (num == null || num.equals("")) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Employee> page = employeeMapper.pageStudentQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 维修员分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageMaintenanceQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //查询的时候做一些不存在的报错信息
        //这里是根据用户名字查询
        String num = employeeMapper.ByName(employeePageQueryDTO.getName());
        if (num == null || num.equals("")) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Employee> page = employeeMapper.pageMaintenanceQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用用户
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //这里判断该账户如果关联了报修，但是报修流程还没有结束，就不能修改它的状态
        Integer statusNum = repairMapper.ByIdStatus(id);
        if (!(statusNum == StatusConstant.Processed)){
            throw new AccountLockedException(MessageConstant.Your_Ticket_Has_Not_Yet_Been_Processed);
        }

        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        employeeMapper.updateStatus(employee);
    }

    /**
     * 新增用户
     * @param employeeDTO
     */
    public void insertEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //用户状态默认为禁用状态
        employee.setStatus(StatusConstant.DISABLE);

        //设置默认密码
        employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);

        //校验身份是否正确
        String role = employee.getRole();
        if (!role.equals(IdentityConstant.Administrator) && !role.equals(IdentityConstant.Student) && !role.equals(IdentityConstant.Maintainer)){
            throw new AccountNotFoundException(MessageConstant.Please_Fill_In_The_Correct_Identity);
        }else {
            employeeMapper.insertEmployee(employee);
        }
    }


    /**
     * 修改用户
     * @param employeePassWordDTO
     */
    public void update(EmployeePassWordDTO employeePassWordDTO) {
        //修改的时候，其余字段不修改就默认原来的值
        //这里也是防止前端崩溃，乱传数据
        Employee employee = employeeMapper.ById(employeePassWordDTO.getId());

        //将用户的报修单的状态循环遍历，判断报修状态是否已经都维修结束
        List<Integer> list = repairMapper.ByNameStatusList(employeePassWordDTO.getName());
        list.forEach(listStatus -> {
            if(!(listStatus == StatusConstant.Processed)){
                throw new AccountLockedException(MessageConstant.Your_Ticket_Has_Not_Yet_Been_Processed);
            }
        });

        //判断角色身份校验
        String role = employee.getRole();
        try {
            if (!role.equals(IdentityConstant.Administrator) && !role.equals(IdentityConstant.Student) && !role.equals(IdentityConstant.Maintainer)){
                throw new AccountNotFoundException(MessageConstant.Please_Fill_In_The_Correct_Identity);
            }
        }catch (Exception e){
            //TODO 前面已经做了防止前端崩溃的情况，可以不用try catch，去处理其他异常情况
            throw new AccountNotFoundException(MessageConstant.Please_Fill_In_The_Correct_Identity);
        }

        //账号唯一不能修改，当前端崩掉的时候，我们后端就是最后一道防线
        // TODO 这里还需要同时满足：在username为空的情况下需要去执行sql；在前端崩掉传了null或者空字符串不能去执行sql
        String username = employeePassWordDTO.getUsername();
        String selectUsername = employeeMapper.selectUsername(employee.getId());
        if (!(username.equals(selectUsername)) || username == null || username.equals("")) {
            throw new AccountLockedException(MessageConstant.Account_Numbers);
        }else {
            try {
                employeeMapper.updateStatus(employee);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除用户
     * @param name
     */
    public void deleteByName(String name) {
        String num = employeeMapper.ByNames(name);
        if (num == null || num.equals("")) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //当前用户如果填写了报修单，状态还没有维修结束，不能删除
        List<Integer> list = repairMapper.ByNameStatusList(name);
        list.forEach(listStatus -> {
            if(!(listStatus == StatusConstant.Processed)){
                throw new AccountLockedException(MessageConstant.Your_Ticket_Has_Not_Yet_Been_Processed);
            }
        });
        employeeMapper.deleteById(name);
    }
}
