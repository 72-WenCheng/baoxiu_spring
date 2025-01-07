package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.RepairPageQueryDTO;
import com.sky.entity.Repair;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepairMapper {

    /**
     * 保存报修
     * @param repair
     */
    @Insert("insert into repair(name, phone, description, image, lou_num, fang_num, repair_status, create_time, update_time, create_user, update_user, repair_num) " +
            "values " +
            "(#{name}, #{phone}, #{description}, #{image}, #{louNum}, #{fangNum}, #{repairStatus}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{repairNum})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Repair repair);

    /**
     * 报修分页查询
     * @param repairPageQueryDTO
     * @return
     */
    Page<Repair> pageQuery(RepairPageQueryDTO repairPageQueryDTO);

    /**
     * 修改报修
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Repair category);

    /**
     * 删除报修
     * @param id
     */
    @Delete("delete from repair where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据派修单id更改报修单的状态
     * @param id
     */
    @Update("update repair set repair_status = 4 where id = #{id}")
    void updateRepairStatus(Long id);

    /**
     * 维修后更改报修状态
     * @param id
     */
    @Update("update repair set repair_status = 5 where id = #{id}")
    void updateMaintenanceStatusTwo(Long id);

    /**
     * 查询该用户报修单是否维修结束
     *
     * @param name
     * @return
     */
    @Select("select  repair_status from repair where name = #{name}")
    List<Integer> deleteEmployeeByName(String name);

    /**
     * 根据id查用户的名字
     * @param id
     */
    @Select("select name from employee where id = #{id}")
    String ByIdNames(Long id);

    /**
     * 根据id查用户的手机号
     * @param id
     */
    @Select("select phone from employee where id = #{id}")
    String ByIdPhones(Long id);

    /**
     * 根据id查询报修单的状态
     * @param id
     */
    @Select("select repair_status from repair where id = #{id}")
    Integer ByIdStatus(Long id);

    /**
     * 根据报修单id查询名字
     * @param id
     * @return
     */
    @Select("select name from repair where id = #{id}")
    String ByIdName(Long id);

    /**
     * 提交报修
     * @param id
     */
    @Update("update repair set repair_status = 3 where id = #{id}")
    void updateCommit(Long id);

    /**
     * 根据id查询派修状态
     * @param id
     */
    @Select("select order_status from dispatch where id = #{id}")
    Integer updateRepairStatuss(Long id);

    /**
     * 修改名字，查看是否报修状态结束
     * @param name
     * @return
     */
    @Select("select repair_status from repair where name = #{name}")
    Integer ByNames(String name);

    /**
     * 根据用户名查询状态，并返回字段数据集合
     * @param name
     * @return
     */
    @Select("select repair_status from repair where name = #{name}")
    List<Integer> ByNameStatusList(String name);

    /**
     * 根据id返回报修实体
     * @param id
     * @return
     */
    @Select("select * from repair where id = #{id}")
    Repair ByIdRepair(Long id);

    /**
     * 报修分页查询，根据报修用户名字查询返回是否有该用户的数据
     * @param name
     * @return
     */
    @Select("select * from employee where name = #{name}")
    String ByName(String name);
}