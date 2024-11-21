package com.yellowcat.backend;

import com.yellowcat.backend.service.EmailService;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import org.springframework.beans.factory.annotation.Autowired;


@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
        emailService.sendEmail("khuongtran2504@gmail.com","khuongtran2504@gmail.com","mailMessage");

        System.out.println("Email đã được gửi thành công.");
    }
}

