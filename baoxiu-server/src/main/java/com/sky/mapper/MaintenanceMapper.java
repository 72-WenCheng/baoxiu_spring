package com.sky.mapper;

import com.sky.entity.Maintenance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MaintenanceMapper {

    /**
     * 新增维修
     * @param maintenance
     */
    @Insert("insert into maintenance(fault_analysis, repair_process, repair_result) " +
            "values " +
            "(#{faultAnalysis}, #{repairProcess}, #{repairResult})")
    //当不采用xml时加上该注解
    @Options(keyProperty = "id", useGeneratedKeys = true)
    void insertMaintenance(Maintenance maintenance);

    /**
     * 外键sql写入
     * @param ids
     */
    @Update("update maintenance set dispatch_id = #{dispatchId} where id = #{id}")
    void updateId(Long ids);

}
