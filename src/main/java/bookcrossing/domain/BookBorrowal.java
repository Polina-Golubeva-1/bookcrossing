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

import java.sql.Timestamp;

@Data
@Entity(name="book_borrowal")
public class BookBorrowal {

        @Id
        @SequenceGenerator(name = "borrowal", sequenceName = "borrowal_id", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrowal")
        private Long id;

        @Column(name = "borrower_id")
        private Long borrowerId;

        @Column(name = "book_id")
        private Long bookId;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "borrow_date")
        private Timestamp borrowDate;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "return_date")
        private Timestamp returnDate;

}
