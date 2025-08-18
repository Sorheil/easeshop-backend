package com.nexora.easeshop.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.application.name}")
    private String fromName;

    @Value("${app.mail.fromAddress}")
    private String fromAddress;

    @Value("${app.mail.replyTo}")
    private String replyTo;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public void sendPasswordResetEmail(String to, String resetLink) throws MessagingException, UnsupportedEncodingException, MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setFrom(new InternetAddress(fromAddress, fromName));
        helper.setReplyTo(replyTo);
        helper.setTo(to);
        helper.setSubject("Réinitialisation de votre mot de passe");

        String htmlContent = """
                <p>Bonjour,</p>
                <p>Vous avez demandé à réinitialiser votre mot de passe.</p>
                <p><a href="%s" style="color: #036D9F; text-decoration: none;">Cliquez ici pour réinitialiser votre mot de passe</a></p>
                <p style="color: red;">⚠ Ce lien est valable pendant <strong>15 minutes</strong>. Passé ce délai, vous devrez refaire la demande.</p>
                <p>Si vous n'êtes pas à l'origine de cette demande, ignorez simplement ce message.</p>
                <p>— L'équipe %s</p>
                """.formatted(resetLink, fromName);

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
