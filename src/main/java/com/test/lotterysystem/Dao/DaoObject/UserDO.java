package com.test.lotterysystem.Dao.DaoObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseDO{

    private String userName;

    private String email;

    private Encrypt phoneNumber;

    private String password;

    private String identity;
}
