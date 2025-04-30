package com.test.lotterysystem.Service.Enum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityPrizeTiersEnum {
    FIRST(1, "一等奖"),
    SECOND(2, "二等奖"),
    THIRD(3, "三等奖");

    private final Integer code;
    private final String tier;

    public static ActivityPrizeTiersEnum forName(String name) {
        for (ActivityPrizeTiersEnum value : ActivityPrizeTiersEnum.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
