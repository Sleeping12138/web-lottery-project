package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.ActivityUserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActivityUserMapperTest {
    @Autowired
    private ActivityUserMapper activityUserMapper;

//    @Test
//    void batchInsertActivityUser() {
//        ActivityUserDO activityUserDO = new ActivityUserDO();
//        activityUserDO.setActivityId(1L);
//        activityUserDO.setUserId(1L);
//        activityUserDO.setUserName("zhangsan");
//        activityUserDO.setStatus("test");
//
//        List<ActivityUserDO> list = new ArrayList<>();
//        list.add(activityUserDO);
//
//        activityUserMapper.batchInsertActivityUser(list);
//    }
}