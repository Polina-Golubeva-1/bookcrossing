package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookRequest;
import bookcrossing.exeption_resolver.BookNotFoundException;
import bookcrossing.exeption_resolver.BookUnavailableException;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.BookRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookRequestService {

    private final BookRequestRepository bookRequestRepository;
    private final BookRepository bookRepository;

    public BookRequestService(BookRequestRepository bookRequestRepository, BookRepository bookRepository) {
        this.bookRequestRepository = bookRequestRepository;
        this.bookRepository = bookRepository;
    }

    public List<BookRequest> getAll() {
        return bookRequestRepository.findAll();
    }

    public Optional<BookRequest> getBookRequestById(Long id) {
        return bookRequestRepository.findById(id);
    }

    public BookRequest createBookRequest(Long requesterId, Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));

            if (book.getStatus() != Book.BookStatus.AVAILABLE) {
                throw new BookUnavailableException("The book with ID " + bookId + " is not available for request.");
            }

            int daysUntilExpiration = 5;

            BookRequest request = new BookRequest();
            request.setRequesterId(requesterId);
            request.setBookId(bookId);
            request.setRequestDate(Timestamp.from(Instant.now()));
            request.setExpirationDate(Timestamp.from(request.getRequestDate().toInstant().plus(daysUntilExpiration, ChronoUnit.DAYS)));

            request = bookRequestRepository.save(request);

            book.setStatus(Book.BookStatus.RESERVED);
            bookRepository.save(book);

            autoCancelReservations();

            return request;
        } catch (BookNotFoundException | BookUnavailableException e) {
            log.error("Error creating book request for bookId={} and requesterId={}", bookId, requesterId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating book request for bookId={} and requesterId={}", bookId, requesterId, e);
            throw e;
        }
    }

    public void autoCancelReservations() {
        Instant now = Instant.now();
        Timestamp currentTimestamp = Timestamp.from(now);

        List<BookRequest> expiredRequests = bookRequestRepository.findByExpirationDateBeforeAndStatus(currentTimestamp, Book.BookStatus.RESERVED);

        for (BookRequest request : expiredRequests) {
            try {
                cancelReservation(request);
            } catch (Exception e) {
                log.error("Error cancelling expired reservation for book with id {}", request.getBookId(), e);
            }
        }
    }

    private void cancelReservation(BookRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());

        optionalBook.ifPresent(book -> {
            if (book.getStatus() == Book.BookStatus.RESERVED) {
                book.setStatus(Book.BookStatus.AVAILABLE);
                bookRepository.save(book);
            }
            bookRequestRepository.deleteById(request.getId());
        });
    }

    public Optional<BookRequest> deleteBookRequestById(Long id) {
        Optional<BookRequest> bookRequestToDelete = bookRequestRepository.findById(id);
        bookRequestToDelete.ifPresent(bookRequest -> bookRequestRepository.deleteById(id));
        return bookRequestToDelete;
    }
}


