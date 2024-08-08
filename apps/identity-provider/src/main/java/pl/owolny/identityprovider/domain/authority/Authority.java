package pl.owolny.identityprovider.domain.authority;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_authority")
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    private String name;

    @ManyToMany(mappedBy = "authorities", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();
}