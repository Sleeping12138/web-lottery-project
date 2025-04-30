package com.test.lotterysystem.Service.Enum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserIdentityEnum {
    ADMIN("管理员"),

    NORMAL("普通用户");

    private final String identity;

    public String getIdentity(){
        return identity;
    }

    public static UserIdentityEnum checkIdentity(String userName){
        for (UserIdentityEnum value : UserIdentityEnum.values()) {
            if(value.name().equalsIgnoreCase(userName)){
                return value;
            }
        }
        return null;
    }

}
