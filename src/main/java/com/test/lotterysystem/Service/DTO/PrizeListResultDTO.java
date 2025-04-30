package com.test.lotterysystem.Service.DTO;

import com.test.lotterysystem.Controller.Result.PrizeInfo;
import lombok.Data;

import java.util.List;

@Data
public class PrizeListResultDTO<T> {
    private Integer total;
    private List<T> records;
}
