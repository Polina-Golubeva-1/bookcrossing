package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookRent;
import bookcrossing.exeption_resolver.BookNotFoundException;
import bookcrossing.exeption_resolver.BookUnavailableException;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.BookRentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static bookcrossing.domain.Variables.daysUntilExpiration;

@Slf4j
@Service
public class BookRentService {

    private final BookRentRepository bookRentRepository;
    private final BookRepository bookRepository;

    public BookRentService(BookRentRepository bookRentRepository, BookRepository bookRepository) {
        this.bookRentRepository = bookRentRepository;
        this.bookRepository = bookRepository;
    }

    public List<BookRent> getAll() {
        return bookRentRepository.findAll();
    }

    public Optional<BookRent> getBookRequestById(Long id) {
        return bookRentRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void rentBook(Long requesterId, Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));

            if (book.getStatus() != Book.BookStatus.AVAILABLE) {
                throw new BookUnavailableException("The book with ID " + bookId + " is not available for request.");
            }

            BookRent request = new BookRent();
            request.setRequesterId(requesterId);
            request.setBookId(bookId);
            request.setRequestDate(Timestamp.from(Instant.now()));
            request.setExpirationDate(Timestamp.from(request.getRequestDate().toInstant().plus(daysUntilExpiration, ChronoUnit.DAYS)));

            bookRentRepository.save(request);

            book.setStatus(Book.BookStatus.RESERVED);
            bookRepository.flush();

        } catch (BookNotFoundException | BookUnavailableException e) {
            log.error("Error creating book request for bookId={} and requesterId={}", bookId, requesterId, e);
            throw new RuntimeException("Error requesting a book", e);
        }
    }

    public Optional<BookRent> deleteBookRequestById(Long id) {
        Optional<BookRent> bookRequestToDelete = bookRentRepository.findById(id);
        bookRequestToDelete.ifPresent(bookRent -> bookRentRepository.deleteById(id));
        return bookRequestToDelete;
    }
}


