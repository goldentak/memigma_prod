package kz.memigma.project.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class MailSenderService {
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void send(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom(from);

        mailSender.send(mailMessage);
    }

    public void sendHtmlEmail(String to, String subject, String code) {
        Context ctx = new Context();
        ctx.setVariable("code", code);

        String html = templateEngine.process("mailContent/mail", ctx);

        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            helper.setText(html, true);

            mailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }
}
