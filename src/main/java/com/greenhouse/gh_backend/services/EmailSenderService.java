package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Sent...");
    }

    public void sendVerificationCode(User user, String verificationCode) {
        String emailBody = "Dear " + user.getFirstName() + ",\n\n"
                + "Thank you for registering with GreenHouse. "
                + "Please use the following code to verify your email address:\n\n"
                + verificationCode + "\n\n"
                + "If you did not register with us, please ignore this email.\n\n"
                + "Regards,\n"
                + "GreenHouse Team";

        sendSimpleEmail(user.getEmail(), "Email Verification Code", emailBody);
    }

    public void sendPasswordResetEmail(User user) {
    }
}
