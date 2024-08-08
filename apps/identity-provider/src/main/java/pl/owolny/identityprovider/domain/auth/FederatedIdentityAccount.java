package pl.owolny.identityprovider.domain.auth;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.owolny.identityprovider.domain.user.User;
import pl.owolny.identityprovider.federation.FederatedProvider;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "external_identity_account")
@Entity
public class FederatedIdentityAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String federatedIdentityId;

    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FederatedProvider provider;

    @CreationTimestamp
    private LocalDateTime connectedAt;

    private boolean isEmailVerified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
