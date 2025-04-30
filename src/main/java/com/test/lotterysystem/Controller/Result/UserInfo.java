package com.test.lotterysystem.Controller.Result;

import com.test.lotterysystem.Dao.DaoObject.Encrypt;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;

    private String userName;

    private String identity;
}
