package com.test.lotterysystem.Service.DTO;

import com.test.lotterysystem.Service.Enum.ActivityPrizeEnum;
import com.test.lotterysystem.Service.Enum.ActivityPrizeTiersEnum;
import lombok.Data;

import java.util.Date;

@Data
public class WinnerRecordInfoDTO {
    private Long winnerId;

    private String winnerName;

    private String prizeName;

    private ActivityPrizeTiersEnum prizeTier;

    private Date winningTime;
}
