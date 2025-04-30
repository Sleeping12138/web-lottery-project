package com.test.lotterysystem.Controller;

import com.test.lotterysystem.Comment.Utils.MyBeanUtil;
import com.test.lotterysystem.Controller.Param.UserPasswordLoginParam;
import com.test.lotterysystem.Controller.Param.UserRegisterParam;
import com.test.lotterysystem.Controller.Param.UserShortMessageLoginParam;
import com.test.lotterysystem.Controller.Result.UserInfo;
import com.test.lotterysystem.Controller.Result.UserLoginResult;
import com.test.lotterysystem.Controller.Result.UserRegisterResult;
import com.test.lotterysystem.Service.DTO.UserInfoDTO;
import com.test.lotterysystem.Service.DTO.UserLoginDTO;
import com.test.lotterysystem.Service.DTO.UserRegisterDTO;
import com.test.lotterysystem.Service.UserService;
import com.test.lotterysystem.Service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 用户注册
     *
     * @param userRegisterParam
     * @return
     */
    @RequestMapping("/register")
    public UserRegisterResult register(@Validated @RequestBody UserRegisterParam userRegisterParam) {
        log.info("userRegisterParam为---------: {}", userRegisterParam);

        UserRegisterDTO register = userService.register(userRegisterParam);
        return MyBeanUtil.transOfUserRegisterResult(register);
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNumber
     * @return
     */
    @RequestMapping("/verificationCode/send")
    public boolean VerificationCodeSend(String phoneNumber) {
        return verificationCodeService.sendVerificationCode(phoneNumber);
    }

    /**
     * 使用密码进行登录
     *
     * @param userPasswordLoginParam
     * @return
     */
    @RequestMapping("/password/login")
    public UserLoginResult userPasswordLogin(@RequestBody @Validated UserPasswordLoginParam userPasswordLoginParam) {
        log.info("UserPasswordLoginParam信息为：------{}", userPasswordLoginParam);
        UserLoginDTO userLoginDTO = userService.login(userPasswordLoginParam);

        return MyBeanUtil.transOfUserLoginResult(userLoginDTO);
    }

    /**
     * 使用验证码进行登录
     *
     * @param userShortMessageLoginParam
     * @return
     */
    @RequestMapping("/shortMessage/login")
    public UserLoginResult userShortMessageLogin(@RequestBody @Validated UserShortMessageLoginParam userShortMessageLoginParam) {
        log.info("UserShortMessageLoginParam信息为：------{}", userShortMessageLoginParam);
        UserLoginDTO userLoginDTO = userService.login(userShortMessageLoginParam);

        return MyBeanUtil.transOfUserLoginResult(userLoginDTO);
    }

    /**
     * 获取指定identity的用户信息
     * @param identity
     * @return
     */
    @RequestMapping("/getUserInfoList")
    public List<UserInfo> getUserInfoList(String identity){
        log.info("identity为：-----"+identity);
        List<UserInfoDTO> userInfoList = userService.getUserInfoList(identity);

        return userInfoList.stream().map(userInfoDTO -> MyBeanUtil.transOfUserInfo(userInfoDTO))
                .collect(Collectors.toList());
    }
}
