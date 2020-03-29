package com.sproutt.eussyaeussyaapi.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JasyptTest {

    private StandardPBEStringEncryptor encryptor;

    private String defualtRawText = "raw";
    private String defualtEncryptedText = "xHtV8owq8HQ7dtAXM3eWsQ==";

    @BeforeEach
    public void setUp() {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("test");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setStringOutputType("base64");
    }

    @Test
    public void decryptTest() throws Exception{

        String decryptedText = encryptor.decrypt(defualtEncryptedText);

        assertThat(decryptedText).isEqualTo(defualtRawText);
    }

    @Test
    public void encryptTest() throws Exception {

        String encryptedText = encryptor.encrypt(defualtRawText);

        assertThat(encryptor.decrypt(encryptedText)).isEqualTo(defualtRawText);
    }

}