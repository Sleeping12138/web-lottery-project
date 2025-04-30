package com.test.lotterysystem.Dao.Mapper;


import com.test.lotterysystem.Controller.Param.UserPasswordLoginParam;
import com.test.lotterysystem.Dao.DaoObject.Encrypt;
import com.test.lotterysystem.Dao.DaoObject.PrizeDO;
import com.test.lotterysystem.Dao.DaoObject.UserDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select count(*) from user where email = #{mail}")
    Integer mailIsUse(String mail);

    @Select("select count(*) from user where phone_number = #{phoneNumber}")
    Integer phoneIsUse(String phoneNumber);

    @Insert("insert into user (user_name, email, phone_number, password, identity) " +
            "values (#{userName},#{email},#{phoneNumber},#{password},#{identity})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(UserDO userDO);

    @Select("select * from user where email = #{loginName}")
    UserDO selectByMail(@Param("loginName") String loginName);


    @Select("select * from user where phone_number = #{phoneNumber}")
    UserDO selectByPhone(@Param("phoneNumber") Encrypt loginName);

    @Select("<script>" +
            "select * from user" +
            "<where>" +
            "    <if test=\"identity != null\">" +
            "        and identity = #{identity}" +
            "    </if>" +
            "</where>" +
            "order by id desc" +
            "</script>")
    List<UserDO> getUserInfoList(String identity);

    @Select("<script> " +
            "select id from user where id in" +
            " <foreach item='item' collection='userIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<Long> selectUserByIds(List<Long> userIds);

    @Select("<script> " +
            "select * from user where id in" +
            " <foreach item='item' collection='userIds' open='(' separator=',' close=')'>" +
            " #{item}" +
            " </foreach>" +
            "</script>")
    List<UserDO> batchSelectByIds(List<Long> userIds);
}
