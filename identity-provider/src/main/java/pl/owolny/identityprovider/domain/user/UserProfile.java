package pl.owolny.identityprovider.domain.user;

import jakarta.persistence.*;
import lombok.*;
import pl.owolny.identityprovider.utils.countrycode.CountryCode;
import pl.owolny.identityprovider.utils.phone.PhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "app_user_profile")
@Entity
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String avatarUrl;

    private String firstName;

    private String lastName;

    private String gender;

    @Embedded
    private PhoneNumber phoneNumber;

    private LocalDate birthDate;

    @Embedded
    private CountryCode countryCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}