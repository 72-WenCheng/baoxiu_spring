package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DispatchDTO;
import com.sky.dto.DispatchPageQueryDTO;
import com.sky.entity.Dispatch;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface DispatchMapper {

    /**
     * 新增派修
     * @param dispatch
     */
    @Insert("insert into dispatch(repair_id, name, dispatch_time, order_status) " +
            "values " +
            "(#{repairId}, #{name}, #{dispatchTime}, #{orderStatus})")
    void insert(Dispatch dispatch);

    /**
     * 派修分页查询
     * @param dispatchPageQueryDTO
     * @return
     */
    Page<Dispatch> pageQueryDispatch(DispatchPageQueryDTO dispatchPageQueryDTO);

    /**
     * 维修后更改派修状态
     * @param id
     */
    @Update("update dispatch set order_status = 7 where id = #{id}")
    void updateMaintenanceStatusNoe(Long id);


    /**
     * 根据id查询派修状态
     * @param id
     * @return
     */
    @Select("select order_status from dispatch where id = #{id}")
    Integer ByIdOrderStatus(Long id);

    /**
     * 根据报修单id查询是否有派修单
     * @param id
     */
    @Select("select * from dispatch where id = #{id}")
    Integer ByIdNum(Long id);

    /**
     * 修改派修
     * @param dispatchDTO
     */
    void UpdateDispatch(DispatchDTO dispatchDTO);

    /**
     * 删除派修
     * @param id
     */
    @Delete("delete from dispatch where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据维修员名字查询是否有数据
     * @param name
     * @return
     */
    @Select("select * from dispatch where name = #{name}")
    String ByName(String name);

    /**
     * 根据id返回派修实体
     * @param id
     * @return
     */
    @Select("select * from dispatch where id = #{id}")
    Dispatch ByIdDispatch(Long id);

    /**
     * 外键sql写入
     * @param ids
     */
    @Update("update dispatch set repair_id = #{repair_id} where id = #{id}")
    void updateId(Long ids);

}
