package edu.email;

import edu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;

    @Value("${app.verification.url}")
    private String verificationBaseUrl; // например: http://localhost:8099/auth/verify?token=

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailVerificationService(JwtUtil jwtUtil, JavaMailSender mailSender) {
        this.jwtUtil = jwtUtil;
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email) {
        String token = jwtUtil.generateTokenVerify(email);
        String link = verificationBaseUrl + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Подтверждение почты");
        message.setText("Привет! Для подтверждения перейди по ссылке:\n" + link);

        mailSender.send(message);
    }
}
