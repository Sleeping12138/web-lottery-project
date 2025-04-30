package com.test.lotterysystem.Service.Impl;

import com.test.lotterysystem.Comment.ErrorCode.ServiceErrorCode;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Comment.Utils.*;
import com.test.lotterysystem.Service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private static final String VERIFICATION_CODE_PREFIX = "VERIFICATION_CODE_";
    private static final long VERIFICATION_CODE_TIMEOUT = 5 * 60L;

    @Autowired
    private SMSUtil smsUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送验证码
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public boolean sendVerificationCode(String phoneNumber) {
        // 校验手机号是否符合格式
        if (!RegexUtil.checkPhone(phoneNumber)) {
            return false;
        }

        // 生成验证码
        String captcha = MyCaptchaUtil.getCaptcha(4);

        // 发送验证码
        Map<String, String> map = new HashMap<>();
        map.put("code", captcha);
        smsUtil.sendMessage("SMS_154950909",
                phoneNumber,
                JacksonUtil.writeValueAsString(map));

        // 存储验证码
        return redisUtil.set(VERIFICATION_CODE_PREFIX + phoneNumber, captcha, VERIFICATION_CODE_TIMEOUT);
    }

    /**
     * 获取指定手机的验证码
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public String getVerificationCode(String phoneNumber) {
        if (!RegexUtil.checkPhone(phoneNumber)) {
            throw new ServiceException(ServiceErrorCode.PHONE_IS_ERROR);
        }
        return redisUtil.get(VERIFICATION_CODE_PREFIX + phoneNumber);
    }
}
