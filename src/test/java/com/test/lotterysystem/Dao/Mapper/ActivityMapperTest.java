package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.ActivityDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActivityMapperTest {

    @Autowired
    private ActivityMapper activityMapper;

    @Test
    void getActivityDetail() {
        ActivityDO activityDetail = activityMapper.getActivityDetail(36L);
        System.out.println(activityDetail);
    }
}