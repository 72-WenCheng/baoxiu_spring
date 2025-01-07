package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据账号查询返回用户实体
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 根据维修员名字查询
     * @param name
     * @return
     */
    @Select("select * from employee where name = #{name}")
    Employee getSelect(String name);

    /**
     * 学生分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageStudentQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 维修员分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageMaintenanceQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 使用动态sql
     * 启用禁用用户
     * 根据id修改
     * 或修改用户其他字段
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(Employee employee);

    /**
     * 新增用户
     * @param employee
     */
    @Insert("insert into employee(name, username, password, phone, sex, id_number, status, role, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{role}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insertEmployee(Employee employee);

    /**
     * 根据id查询账号
     * @param id
     * @return
     */
    @Select("select username from employee where id = #{id}")
    String selectUsername(Long id);

    /**
     * 根据id删除用户
     * @param name
     */
    @Delete("delete from employee where name = #{name}")
    void deleteById(String name);

    /**
     * 根据id查询是否存在该用户
     * @param id
     */
    @Select("select * from employee where id = #{id}")
    String ByIdEmployee(Long id);


    /**
     * 根据id查询并返回用户实体
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee ById(Long id);

    /**
     * 用户分页查询，根据用户名字查询返回是否有该用户的数据
     * @param name
     * @return
     */
    @Select("select * from employee where name = #{name}")
    String ByName(String name);

    /**
     * 根据用户名查询是否有该用户
     * @param name
     * @return
     */
    @Select("select * from employee where name = #{name}")
    String ByNames(String name);
}
