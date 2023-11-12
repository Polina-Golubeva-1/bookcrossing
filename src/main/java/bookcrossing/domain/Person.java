package bookcrossing.domain;



import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;


@Data
@Entity(name="person")
public class Person {

    @Id
    @SequenceGenerator(name = "personsId", sequenceName = "person_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personsId")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Min(value = 15)
    @Max(value = 120)
    @Column(name = "age")
    private Integer age;

    @Size(min = 3, max = 15)
    @Column(name = "phone")
    private Integer phone;

    @Size(min = 6, max = 200)
    @Pattern(regexp = "[A-z]*")
    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Timestamp created;

    @Column(name = "rating")
    private Integer rating;

    @Pattern(regexp = "[A-z]*")
    @Column(name = "address")
    private String address;

}
