package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Transactional
    public String sendAuthEmail(String to) {
        String authenticationCode = RandomGenerator.createAuthenticationCode();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("으쌰으쌰 계정 인증");
        simpleMailMessage.setText("인증 코드: " + authenticationCode);

        try {
            mailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new MailSendException("메일 전송 에러");
        }

        return authenticationCode;
    }

    @Override
    public void sendChangedPasswordMail() {

    }
}
