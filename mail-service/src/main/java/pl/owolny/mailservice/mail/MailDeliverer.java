package pl.owolny.mailservice.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailDeliverer {

    private final JavaMailSender javaMailSender;

    public MailDeliverer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(MimeMessage mail) {
        log.info("Sending new mail");
        javaMailSender.send(mail);
    }
}