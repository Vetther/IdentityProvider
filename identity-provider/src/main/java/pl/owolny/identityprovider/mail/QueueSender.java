package pl.owolny.identityprovider.mail;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.owolny.mailservice.mail.MailDto;

@Component
public class QueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public QueueSender(RabbitTemplate rabbitTemplate,
                       @Value("${spring.rabbitmq.exchange}") String exchange,
                       @Value("${spring.rabbitmq.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void send(MailDto mail) {
        rabbitTemplate.convertAndSend(exchange, routingKey, mail);
    }

}