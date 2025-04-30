package com.test.lotterysystem.Dao.DaoObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 将密码封装一层
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encrypt {
    private String value;
}
