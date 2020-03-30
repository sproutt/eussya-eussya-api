package com.sproutt.eussyaeussyaapi.application;

public interface MailService {

    void sendChangedPasswordMail();

    void sendAuthEmail(String email, String authCode);
}
