package bookcrossing.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity(name = "book")
public class Book {
    @Id
    @SequenceGenerator(name = "myBook", sequenceName = "book_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "myBook")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "author")
    private String author;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Timestamp created;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;

    public enum BookStatus {
        AVAILABLE,
        RESERVED,
        ON_HAND
    }

    @ManyToOne
    private Person owner;

}
