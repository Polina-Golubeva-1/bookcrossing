package bookcrossing.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
@Data
@Component
@Entity(name="book_request")
public class BookRequest {

        @Id
        @SequenceGenerator(name = "request", sequenceName = "request_id", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request")
        private Long id;

        @Column(name = "requester_id")
        private Long requesterId;

        @Column(name = "book_id")
        private Long bookId;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "request_date")
        private Timestamp requestDate;

}
