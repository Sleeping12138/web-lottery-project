package com.test.lotterysystem.Service.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityEnum {
    PROCEED(1,"进行中"),
    FINISH(0,"已完成");

    private final Integer code;
    private final String status;

    public static ActivityEnum forName(String name){
        for (ActivityEnum value : ActivityEnum.values()) {
            if(value.name().equals(name)){
                return value;
            }
        }
        return null;
    }
}
