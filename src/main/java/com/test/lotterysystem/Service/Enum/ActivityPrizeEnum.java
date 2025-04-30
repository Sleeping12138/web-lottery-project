package com.test.lotterysystem.Service.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityPrizeEnum {
    INIT(1, "未被抽取"),

    COMPLETED(0, "已被抽取");

    private final Integer code;
    private final String status;

    public static ActivityPrizeEnum forName(String name) {
        for (ActivityPrizeEnum value : ActivityPrizeEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
