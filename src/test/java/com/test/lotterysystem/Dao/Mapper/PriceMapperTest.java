package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Dao.DaoObject.priceDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceMapperTest {

    @Autowired
    private PrizeMapper prizeMapper;
    @Test
    public void create(){
        priceDO priceDO = new priceDO();
        priceDO.setPrizeName("test");
        priceDO.setDescription("null");
        priceDO.setImageUrl("afijowfwe.jpg");
        priceDO.setPrice(new BigDecimal("78.9"));

        prizeMapper.create(priceDO);
    }
}