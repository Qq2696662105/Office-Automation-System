package com.dataKing.security.custom;

import com.dataKing.common.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ClassName: CustomMd5PasswordEncoder
 * Package: com.dataKing.security.custom
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/17 0017 15:09
 * @Version 1.0
 */

@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}
