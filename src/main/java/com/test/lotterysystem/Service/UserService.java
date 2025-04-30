package com.test.lotterysystem.Service;

import com.test.lotterysystem.Controller.Param.UserLoginParam;
import com.test.lotterysystem.Controller.Param.UserRegisterParam;
import com.test.lotterysystem.Controller.Param.UserShortMessageLoginParam;
import com.test.lotterysystem.Controller.Result.UserRegisterResult;
import com.test.lotterysystem.Service.DTO.UserInfoDTO;
import com.test.lotterysystem.Service.DTO.UserLoginDTO;
import com.test.lotterysystem.Service.DTO.UserRegisterDTO;

import java.util.List;

public interface UserService {
    UserRegisterDTO register(UserRegisterParam userRegisterParam);

    UserLoginDTO login(UserLoginParam userLoginParam);

    List<UserInfoDTO> getUserInfoList(String identity);
}
