package com.sproutt.eussyaeussyaapi.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendChangedPasswordMail() {

    }

    @Override
    public void sendAuthEmail(String email, String authCode) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("으쌰으쌰 계정 인증");
        message.setText("인증 코드: " + authCode);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new MailSendException("메일 전송 에러");
        }
    }
}
