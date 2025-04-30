package com.test.lotterysystem.Controller.Result;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WinnerRecordInfo implements Serializable {
    private Long winnerId;

    private String winnerName;

    private String prizeName;

    private String prizeTier;

    private Date winningTime;

}
