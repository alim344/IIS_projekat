package com.example.autoskola.service;


import com.example.autoskola.model.User;
import com.example.autoskola.model.VerificationToken;
import com.example.autoskola.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private Environment env;


    @Autowired
    private JavaMailSender mailSender;

    public void verifyMail(User user) {

        String token = UUID.randomUUID().toString();

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

        VerificationToken vToken =  createVerificationToken(user, token, expiryDate);

        sendMail(token, user.getEmail());

    }

    private VerificationToken createVerificationToken(User user, String token, LocalDateTime expiryDate) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(expiryDate);
        verificationToken.setUser(user);
        return verificationTokenRepository.save(verificationToken);
    }

    private void sendMail(String token, String userEmail) {

        String activationLink = UriComponentsBuilder.fromUriString("http://localhost:8080/auth/verify")
                .queryParam("token", token)
                .toUriString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setSubject("Account Activation");
        message.setText("Click the link to activate your account: " + activationLink);
        mailSender.send(message);



    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void deleteToken(VerificationToken vToken) {
        verificationTokenRepository.delete(vToken);
    }


}
