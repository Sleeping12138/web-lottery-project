package com.test.lotterysystem.Controller.Result;

import lombok.Data;

import java.util.List;

@Data
public class PrizeListResult {
    private Integer total;
    private List<PrizeInfo> records;
}
