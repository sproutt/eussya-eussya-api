package com.sproutt.eussyaeussyaapi.application;

public interface MailService {

    void sendAuthenticationMail(String to);

    void sendChangedPasswordMail();
}
