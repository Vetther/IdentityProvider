package pl.owolny.identityprovider.utils.countrycode;

import jakarta.persistence.Embeddable;

@Embeddable
public record CountryCode(String code) {

    public CountryCode(String code) {
        this.code = validateCountryCode(code);
    }

    private static String validateCountryCode(String code) {
        if (code == null) {
            return null;
        }
        if (!code.matches("^[A-Z]{2}$")) {
            throw new IllegalArgumentException("Invalid country code format");
        }
        // correct example: PL
        return code;
    }
}
