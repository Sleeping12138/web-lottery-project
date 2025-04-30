package com.test.lotterysystem.Controller.Param;

import lombok.Data;

@Data
public class PageParam {
    private Integer currentPage;
    private Integer pageSize;

    public int offset(Integer currentPage, Integer pageSize) {
        return (currentPage - 1) * pageSize;
    }
}
