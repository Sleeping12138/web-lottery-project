package com.test.lotterysystem.Service.DTO;

import com.test.lotterysystem.Dao.DaoObject.Encrypt;
import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;

    private String userName;

    private String email;

    private Encrypt phoneNumber;

    private String password;

    private String identity;
}
