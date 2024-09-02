package pl.owolny.mailservice.mail;

import lombok.Builder;

import java.util.Map;

@Builder
public record MailDto(
        Template template,
        String recipient,
        String title,
        Map<String, Object> templateProperties
) {
}