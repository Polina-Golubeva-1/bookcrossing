package bookcrossing.security.domain;

import bookcrossing.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity(name = "security")
@Data
@Component
public class SecurityCredentials {
    @Id
    @SequenceGenerator(name = "security_generator", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "security_generator")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String personPassword;

    @Column(name = "person_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "person_id")
    private Long personId;
}
