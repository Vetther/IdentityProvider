package pl.owolny.identityprovider.utils.phone;

import jakarta.persistence.Embeddable;

@Embeddable
public record PhoneNumber(String value) {

    public PhoneNumber(String value) {
        this.value = validateAndNormalizePhoneNumber(value);
    }

    private static String validateAndNormalizePhoneNumber(String value) {
        if (value == null) {
            return null;
        }
        if (!value.matches("^\\+\\d{1,3}\\d{9,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        // correct example: +48123456789
        return value;
    }
}
