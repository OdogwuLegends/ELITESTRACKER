package com.capstoneproject.ElitesTracker.emailConfig;

import com.capstoneproject.ElitesTracker.models.EliteUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String token, EliteUser eliteUser) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset";
        String senderName = "ElitesTracker";
        String mailContent = "<p> Hi "+ eliteUser.getFirstName()+", </p>"+
                "Please enter the 4-digit token below on your dashboard to reset your password.<br><br></p>"+
                token+
                "<p> Thank you. <br> ElitesTracker";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("emeralds161996@gmail.com",senderName);
        messageHelper.setTo(eliteUser.getSemicolonEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent,true);
        mailSender.send(message);
    }
}
