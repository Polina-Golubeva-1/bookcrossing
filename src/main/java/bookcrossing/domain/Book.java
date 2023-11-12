package bookcrossing.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
@Entity(name="book")
public class Book {
    @Id
    @SequenceGenerator(name = "myBook", sequenceName = "book_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "myBook")
    private Long id;

    @Size(min = 2, max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "author")
    private String author;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "availabe")
    private Boolean availabe;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Timestamp created;


}
