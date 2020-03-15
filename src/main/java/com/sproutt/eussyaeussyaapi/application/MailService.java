package com.sproutt.eussyaeussyaapi.application;

public interface MailService {

    String sendAuthEmail(String to);


    void sendChangedPasswordMail();
}
