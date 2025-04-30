package com.test.lotterysystem.Service;

public interface VerificationCodeService {
    boolean sendVerificationCode(String phoneNumber);

    String getVerificationCode(String phoneNumber);
}
