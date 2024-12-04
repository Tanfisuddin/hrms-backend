package com.imw.admin.services.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendUserCredentialsEmail(String organizationName, String name, String username, String password, String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Welcome to " + organizationName + "'s HRMS Portal!");

        String mailContent = "Dear " + name + ",\n\n"
                + "Welcome to the HRMS portal of " + organizationName + "!\n\n"
                + "Your account has been successfully created. Here are your login credentials:\n\n"
                + "Username: " + username + "\n"
                + "Password: " + password + "\n\n"
                + "Please log in to the HRMS portal using the provided credentials at https://web.organisationhr.com We recommend changing your password upon first login for security purposes.\n\n"
                + "Thank you, and we look forward to seeing you use our HRMS system!\n\n"
                + "Best regards,\nThe " + organizationName + " HRMS Team";

        message.setText(mailContent);

        javaMailSender.send(message);
    }

    @Async
    public void sendUserForgetPasswordEmail(String organizationName, String name, String userEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Password reset OTP");

        String mailContent = "Dear " + name + ",\n\n"
                + "You have requested a password reset for your account.\n"
                + "Your One-Time Password (OTP) is: " + otp + "\n"
                + "Please use this OTP to reset your password.\n"
                + "If you did not request this password reset, please ignore this email.\n\n"
                + "Thank you,\n"
                + "Best regards,\nThe " + organizationName + " HRMS Team";

        message.setText(mailContent);

        javaMailSender.send(message);
    }
}
