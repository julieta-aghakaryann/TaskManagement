package com.management.auth.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;

@Configuration
public class PasswordEncoderBean extends MessageDigestPasswordEncoder {

    public PasswordEncoderBean() {
        super("MD5");
    }
}