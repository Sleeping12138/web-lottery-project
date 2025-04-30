package com.test.lotterysystem.Service.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ActivityListResultDTO<T> {
    private Integer total;
    private List<T> records;
}
