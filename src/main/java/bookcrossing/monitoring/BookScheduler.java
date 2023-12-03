package bookcrossing.monitoring;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookRent;
import bookcrossing.repository.BookRentRepository;
import bookcrossing.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BookScheduler {
    private final BookRentRepository bookRentRepository;
    private final BookRepository bookRepository;


    public BookScheduler(BookRentRepository bookRentRepository, BookRepository bookRepository) {
        this.bookRentRepository = bookRentRepository;
        this.bookRepository = bookRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void autoCancelReservations() {
        Instant now = Instant.now();
        Timestamp currentTimestamp = Timestamp.from(now);

        List<BookRent> expiredRequests = bookRentRepository.findByExpirationDateBeforeAndStatus(currentTimestamp, Book.BookStatus.RESERVED);

        for (BookRent request : expiredRequests) {
            try {
                cancelReservation(request);
            } catch (Exception e) {
                log.error("Error cancelling expired reservation for book with id {}", request.getBookId(), e);
            }
        }
    }


    private void cancelReservation(BookRent request) {
        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());

        optionalBook.ifPresent(book -> {
            if (book.getStatus() == Book.BookStatus.RESERVED) {
                book.setStatus(Book.BookStatus.AVAILABLE);
                bookRepository.save(book);
            }
            bookRentRepository.deleteById(request.getId());
        });
    }
}

