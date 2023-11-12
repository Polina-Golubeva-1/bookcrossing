package bookcrossing.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity(name="black_list")
public class BlackList {

        @Id
        @SequenceGenerator(name = "list", sequenceName = "list_id", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "list")
        private Long id;

        @Size(max = 3)
        @Column(name = "rating")
        private Integer rating;

        @Column(name = "reason_for_blocking")
        private String reason;

        @Column(name = "person_id")
        private Long personId;

    }

