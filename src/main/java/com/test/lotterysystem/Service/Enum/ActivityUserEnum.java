package com.test.lotterysystem.Service.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityUserEnum {
    INIT(1, "未抽取奖品"),

    COMPLETED(0, "已被抽取奖品");

    private final Integer code;
    private final String status;
}
