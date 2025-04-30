package com.test.lotterysystem.Dao.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.lotterysystem.Dao.DaoObject.ActivityDO;
import com.test.lotterysystem.Dao.DaoObject.ActivityListDO;
import com.test.lotterysystem.Dao.DaoObject.PrizeDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ActivityMapper {

    @Insert("insert into activity (activity_name, description, status)" +
            " values (#{activityName}, #{description}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty ="id", keyColumn ="id")
    void insert(ActivityDO activityDO);

    @Select("select count(*) from activity")
    Integer count();


    @Select("select * from activity order by id desc limit #{offset} , #{pageSize}")
    List<ActivityListDO> getActivityList(Integer offset, Integer pageSize);

    @Select("select * from activity where id = #{id}")
    ActivityDO getActivityDetail(@Param("id") Long activityId);

    @Update("update activity set status = #{name} where id = #{activityId}")
    void updateStatus(Long activityId, String name);
}
