package com.sproutt.eussyaeussyaapi.application;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendAuthenticationMail(String to) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("으쌰으쌰 계정 인증");
        simpleMailMessage.setText("인증 코드");

        mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendChangedPasswordMail() {

    }
}
