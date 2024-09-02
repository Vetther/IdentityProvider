package pl.owolny.mailservice.mail;

import lombok.Getter;

@Getter
public enum Template {
    EXAMPLE("Welcome!"),
    EMAIL_VERIFICATION("Email verification"),
    LINK_ACCOUNT_VERIFICATION("Link account verification");

    final String title;

    Template(String title) {
        this.title = title;
    }
}