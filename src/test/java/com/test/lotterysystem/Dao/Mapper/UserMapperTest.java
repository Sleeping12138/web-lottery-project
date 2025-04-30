package com.test.lotterysystem.Dao.Mapper;

import com.test.lotterysystem.Comment.Utils.SecurityUtil;
import com.test.lotterysystem.Dao.DaoObject.Encrypt;
import com.test.lotterysystem.Dao.DaoObject.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void selectByMail() {
        System.out.println(userMapper.selectByMail("12345@qq.com"));
    }

    @Test
    void selectByPhone() {
        System.out.println(userMapper.selectByPhone(new Encrypt("13675498018")));
    }


    @Test
    void insert() {
        UserDO userDO = new UserDO();
        userDO.setUserName("test");
        userDO.setEmail("1239@qq.com");
        userDO.setPhoneNumber(new Encrypt("19016414259"));
        userDO.setPassword(SecurityUtil.encrypt("123456"));
        userDO.setIdentity("ADMIN");
        userMapper.insert(userDO);
    }

    @Test
    void getUserInfoList() {
        List<UserDO> admin = userMapper.getUserInfoList("ADMIN");
        System.out.println(admin);
    }

    @Test
    void selectUserByIds() {
        List<Long> list = Arrays.asList(40L, 44L, 46L);
        List<Long> ret = userMapper.selectUserByIds(list);
        System.out.println(ret);
    }
}