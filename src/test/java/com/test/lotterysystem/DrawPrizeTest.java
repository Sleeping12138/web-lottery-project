package com.test.lotterysystem;

import com.test.lotterysystem.Controller.Param.DrawPrizeParam;
import com.test.lotterysystem.Service.ActivityStatusChange.ActivityStatusChange;
import com.test.lotterysystem.Service.DTO.ConvertActivityStatusDTO;
import com.test.lotterysystem.Service.DrawPrizeService;
import com.test.lotterysystem.Service.Enum.ActivityEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityUserEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class DrawPrizeTest {

    @Autowired
    private DrawPrizeService drawPrizeService;

    @Test
    public void test() {

        DrawPrizeParam drawPrizeParam = new DrawPrizeParam();
        drawPrizeParam.setActivityId(36L);
        drawPrizeParam.setPrizeId(23L);
        drawPrizeParam.setPrizeTiers("FIRST");
        drawPrizeParam.setWinningTime(new Date());

        List<DrawPrizeParam.Winner> list = new ArrayList<>();
        DrawPrizeParam.Winner winner = new DrawPrizeParam.Winner();
        winner.setUserId(51L);
        winner.setUserName("zjy");
        list.add(winner);
        drawPrizeParam.setWinnerList(list);

        drawPrizeService.saveDrawPrizeInfo(drawPrizeParam);

        System.out.println("发送完成!!!");
    }
}
