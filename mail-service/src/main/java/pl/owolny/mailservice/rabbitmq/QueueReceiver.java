package pl.owolny.mailservice.rabbitmq;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.owolny.mailservice.mail.MailCreator;
import pl.owolny.mailservice.mail.MailDeliverer;
import pl.owolny.mailservice.mail.MailDto;

@Slf4j
@Component
@RabbitListener(queues = "${spring.rabbitmq.queue}")
@RequiredArgsConstructor
public class QueueReceiver {

    private final MailCreator mailCreator;
    private final MailDeliverer mailDeliverer;

    @RabbitHandler
    public void receive(@Payload MailDto mail) {
        log.info("Received new event");
        MimeMessage mailToSend = mailCreator.createMailToSend(mail);
        mailDeliverer.sendMail(mailToSend);
    }
}