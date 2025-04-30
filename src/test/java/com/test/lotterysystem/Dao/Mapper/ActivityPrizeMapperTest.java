package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.ActivityPrizeDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActivityPrizeMapperTest {
    @Autowired
    private ActivityPrizeMapper activityPrizeMapper;

    @Test
    void batchInsertActivityPrize() {
        List<ActivityPrizeDO> list = new ArrayList<>();
        ActivityPrizeDO temp = new ActivityPrizeDO();
        temp.setActivityId(1L);
        temp.setPrizeId(1L);
        temp.setPrizeAmount(3L);
        temp.setPrizeTiers("first");
        temp.setStatus("test");
        list.add(temp);

        activityPrizeMapper.batchInsertActivityPrize(list);
    }
}