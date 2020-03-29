package com.sproutt.eussyaeussyaapi.object;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptedResourceGenerator {

    public final static String encryptedGitTokenForTest ="avZ0DLSUB0GCauM13iEHHDL5rKNB7YjRseDugBm5kL79EoNU8o9zdPw8omdeBIxzRfeLNtsgor4=";

    public static String getGitToken() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("test");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setStringOutputType("base64");

        return encryptor.decrypt(encryptedGitTokenForTest);
    }

}
