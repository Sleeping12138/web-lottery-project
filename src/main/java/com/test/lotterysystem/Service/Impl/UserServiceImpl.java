package com.test.lotterysystem.Service.Impl;

import com.test.lotterysystem.Comment.ErrorCode.ServiceErrorCode;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Comment.Utils.JWTUtil;
import com.test.lotterysystem.Comment.Utils.RegexUtil;
import com.test.lotterysystem.Comment.Utils.SecurityUtil;
import com.test.lotterysystem.Controller.Param.UserLoginParam;
import com.test.lotterysystem.Controller.Param.UserPasswordLoginParam;
import com.test.lotterysystem.Controller.Param.UserRegisterParam;
import com.test.lotterysystem.Controller.Param.UserShortMessageLoginParam;
import com.test.lotterysystem.Controller.Result.UserRegisterResult;
import com.test.lotterysystem.Dao.DaoObject.Encrypt;
import com.test.lotterysystem.Dao.DaoObject.UserDO;
import com.test.lotterysystem.Dao.Mapper.UserMapper;
import com.test.lotterysystem.Service.DTO.UserInfoDTO;
import com.test.lotterysystem.Service.DTO.UserLoginDTO;
import com.test.lotterysystem.Service.DTO.UserRegisterDTO;
import com.test.lotterysystem.Service.Enum.UserIdentityEnum;
import com.test.lotterysystem.Service.UserService;
import com.test.lotterysystem.Service.VerificationCodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 用户注册
     *
     * @param userRegisterParam
     * @return
     */
    @Override
    public UserRegisterDTO register(UserRegisterParam userRegisterParam) {
        // 检测数据是否符合条件
        checkRegisterInfo(userRegisterParam);

        // 创建对象
        UserDO userDO = new UserDO();
        userDO.setUserName(userRegisterParam.getName());
        userDO.setEmail(userRegisterParam.getMail());
        userDO.setPhoneNumber(new Encrypt(userRegisterParam.getPhoneNumber()));
        if (StringUtils.hasText(userRegisterParam.getPassword())) {
            userDO.setPassword(SecurityUtil.encrypt(userRegisterParam.getPassword()));
        }
        userDO.setIdentity(userRegisterParam.getIdentity());

        // 进行插入
        userMapper.insert(userDO);

        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUserId(userDO.getId());
        return userRegisterDTO;
    }

    private void checkRegisterInfo(UserRegisterParam userRegisterParam) {
        // 注册信息为空
        if (userRegisterParam == null) throw new ServiceException(ServiceErrorCode.REGISTER_INFO_IS_EMPTY);


        // 检测邮箱
        if (!RegexUtil.checkMail(userRegisterParam.getMail()))
            throw new ServiceException(ServiceErrorCode.MAIL_IS_ERROR);

        if (mailIsUse(userRegisterParam.getMail()))
            throw new ServiceException(ServiceErrorCode.MAIL_IS_USE);


        // 检测手机号
        if (!RegexUtil.checkPhone(userRegisterParam.getPhoneNumber()))
            throw new ServiceException(ServiceErrorCode.PHONE_IS_ERROR);

        if (phoneIsUse(userRegisterParam.getPhoneNumber()))
            throw new ServiceException(ServiceErrorCode.PHONE_IS_USE);


        // 检测用户身份是否正确
        if (UserIdentityEnum.checkIdentity(userRegisterParam.getIdentity()) == null)
            throw new ServiceException(ServiceErrorCode.IDENTITY_IS_ERROR);


        // 检测密码
        if (userRegisterParam.getIdentity().equalsIgnoreCase(UserIdentityEnum.ADMIN.name())
                && !StringUtils.hasText(userRegisterParam.getPassword())) {
            throw new ServiceException(ServiceErrorCode.PASSWORD_IS_EMPTY);
        }

        if (StringUtils.hasText(userRegisterParam.getPassword())
                && !RegexUtil.checkPassword(userRegisterParam.getPassword())) {
            throw new ServiceException(ServiceErrorCode.PASSWORD_ERROR);
        }


    }

    private boolean phoneIsUse(String phoneNumber) {
        return userMapper.phoneIsUse(phoneNumber) > 0;
    }

    private boolean mailIsUse(String mail) {
        return userMapper.mailIsUse(mail) > 0;
    }

    /**
     * 用户登录
     *
     * @param userLoginParam
     * @return
     */
    @Override
    public UserLoginDTO login(UserLoginParam userLoginParam) {
        if (userLoginParam == null) {
            throw new ServiceException(ServiceErrorCode.LOGIN_INFO_IS_EMPTY);
        }
        UserLoginDTO userLoginDTO = null;
        if (userLoginParam instanceof UserPasswordLoginParam loginParam) {
            // 密码登录

            // 查找数据库
            userLoginDTO = selectByPassword(loginParam);
        } else if (userLoginParam instanceof UserShortMessageLoginParam loginParam) {
            // 验证码登录

            // 查找数据库
            userLoginDTO = selectByShortMessage(loginParam);
        } else {
            throw new ServiceException(ServiceErrorCode.LOGIN_INFO_NOT_EXIST);
        }

        return userLoginDTO;
    }


    private UserLoginDTO selectByShortMessage(UserShortMessageLoginParam loginParam) {
        if (!RegexUtil.checkPhone(loginParam.getPhoneNumber())) {
            throw new ServiceException(ServiceErrorCode.PHONE_IS_ERROR);
        }

        UserDO userDO = userMapper.selectByPhone(new Encrypt(loginParam.getPhoneNumber()));

        if (userDO == null) {
            throw new ServiceException(ServiceErrorCode.USER_NOT_EXIST);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity().equalsIgnoreCase(userDO.getIdentity())) {
            throw new ServiceException(ServiceErrorCode.IDENTITY_IS_ERROR);
        } else if (!loginParam.getVerificationCode().equals(verificationCodeService.getVerificationCode(loginParam.getPhoneNumber()))) {
            throw new ServiceException(ServiceErrorCode.VERIFICATION_CODE_ERROR);
        }

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        Map<String, Object> map = new HashMap<>();
        map.put("id", userDO.getId());
        map.put("identity", userDO.getIdentity());

        String token = JWTUtil.genJwt(map);
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(userDO.getIdentity());
        return userLoginDTO;
    }

    private UserLoginDTO selectByPassword(UserPasswordLoginParam loginParam) {
        UserDO userDO = null;
        if (RegexUtil.checkMail(loginParam.getLoginName())) {
            // 通过邮箱进行查询
            userDO = userMapper.selectByMail(loginParam.getLoginName());
        } else if (RegexUtil.checkPhone(loginParam.getLoginName())) {
            // 通过手机号进行查询
            userDO = userMapper.selectByPhone(new Encrypt(loginParam.getLoginName()));
        } else {
            throw new ServiceException(ServiceErrorCode.LOGIN_NOT_EXIST);
        }


        // 校验登录信息
        if (userDO == null) {
            throw new ServiceException(ServiceErrorCode.USER_NOT_EXIST);
        } else if (StringUtils.hasText(loginParam.getMandatoryIdentity())
                && !loginParam.getMandatoryIdentity().equalsIgnoreCase(userDO.getIdentity())) {
            throw new ServiceException(ServiceErrorCode.IDENTITY_IS_ERROR);
        } else if (!SecurityUtil.verify(loginParam.getPassword(), userDO.getPassword())) {
            throw new ServiceException(ServiceErrorCode.PASSWORD_ERROR);
        }

        // 创建返回值
        UserLoginDTO userLoginDTO = new UserLoginDTO();

        Map<String, Object> map = new HashMap<>();
        map.put("id", userDO.getId());
        map.put("identity", userDO.getIdentity());
        String token = JWTUtil.genJwt(map);
        userLoginDTO.setToken(token);
        userLoginDTO.setIdentity(userDO.getIdentity());
        return userLoginDTO;
    }


    /**
     * 获取指定用户信息
     *
     * @param identity
     * @return
     */
    @Override
    public List<UserInfoDTO> getUserInfoList(String identity) {
        if (identity!=null && !identity.equals("ADMIN") && !identity.equals("NORMAL")) {
            throw new ServiceException(ServiceErrorCode.GET_USER_IDENTITY_ERROR);
        }

        List<UserDO> userDOS = userMapper.getUserInfoList(identity);
        return userDOS.stream().map(userDO -> {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            BeanUtils.copyProperties(userDO, userInfoDTO);
            return userInfoDTO;
        }).collect(Collectors.toList());
    }
}
