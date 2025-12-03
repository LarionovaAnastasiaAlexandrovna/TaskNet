package edu.email;

import edu.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JwtUtil jwtUtil;
    private final JavaMailSender mailSender;

    @Value("${app.verification.url}")
    private String verificationBaseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String email) {
        String token = jwtUtil.generateTokenVerify(email);
        String link = verificationBaseUrl + token;

        var message = createVerificationMessage(email, link);
        mailSender.send(message);
    }

    private SimpleMailMessage createVerificationMessage(String email, String link) {
        var message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Подтверждение почты");
        message.setText(createEmailText(link));
        return message;
    }

    private String createEmailText(String link) {
        return """
            Здравствуйте!
            
            Для завершения регистрации, пожалуйста, подтвердите вашу электронную почту.
            
            Перейдите по ссылке для подтверждения:
            %s
            
            Ссылка действительна в течение 24 часов.
            
            С уважением,
            Команда TaskNet
            """.formatted(link);
    }
}
