package pl.owolny.identityprovider.mail;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import pl.owolny.identityprovider.domain.otp.OTP;
import pl.owolny.mailservice.mail.MailDto;
import pl.owolny.mailservice.mail.Template;

import java.util.Map;

@Component
@Getter
@Setter
public class MailService {

    private final QueueSender queueSender;

    public MailService(QueueSender queueSender) {
        this.queueSender = queueSender;
    }

    public MailDto createLinkAccountEmail(@NotNull String username, @NotNull String email, @NotNull OTP otp) {

        Template template = Template.LINK_ACCOUNT_VERIFICATION;

        Map<String, Object> properties = Map.of(
                "username", username,
                "otp", otp.otpCode()
        );

        return MailDto.builder()
                .template(template)
                .recipient(email)
                .title(template.getTitle())
                .templateProperties(properties)
                .build();
    }

    public void sendMail(@NotNull MailDto mail) {
        if (mail == null) {
            throw new IllegalArgumentException();
        }

        queueSender.send(mail);
    }

}