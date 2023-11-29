package bookcrossing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity(name = "black_list")
public class BlackList {

    @Id
    @SequenceGenerator(name = "list", sequenceName = "list_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "list")
    private Long id;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "person_id")
    private Long personId;

}

