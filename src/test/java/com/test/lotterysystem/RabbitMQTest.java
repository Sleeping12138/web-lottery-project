package com.test.lotterysystem;

import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Service.DrawPrizeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class RabbitMQTest {
    @Autowired
    private DrawPrizeService drawPrizeService;

    @Test
    public void test(){
        DrawPrizeParam drawPrizeParam = new DrawPrizeParam();
        drawPrizeParam.setActivityId(1L);
        drawPrizeParam.setPrizeId(1L);
        drawPrizeParam.setPrizeTiers("xxx");
        drawPrizeParam.setWinningTime(new Date());

        List<DrawPrizeParam.Winner> list = new ArrayList<>();
        DrawPrizeParam.Winner winner = new DrawPrizeParam.Winner();
        winner.setUserId(1L);
        winner.setUserName("xxx");
        list.add(winner);
        drawPrizeParam.setWinnerList(list);

        drawPrizeService.saveDrawPrizeInfo(drawPrizeParam);
    }

}
