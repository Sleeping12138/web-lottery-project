package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.PrizeDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrizeMapperTest {
    @Autowired
    private PrizeMapper prizeMapper;

    @Test
    void getPrizeList() {
        List<PrizeDO> prizeList = prizeMapper.getPrizeList(1, 10);
        System.out.println(prizeList);
    }
}