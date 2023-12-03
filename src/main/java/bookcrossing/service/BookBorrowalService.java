package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookBorrowal;
import bookcrossing.domain.BookRequest;
import bookcrossing.domain.Person;
import bookcrossing.exeption_resolver.BookNotFoundException;
import bookcrossing.exeption_resolver.BookUnavailableException;
import bookcrossing.repository.BookBorrowalRepository;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.BookRequestRepository;
import bookcrossing.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookBorrowalService {

    private final BookBorrowalRepository bookBorrowalRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final BookRequestRepository bookRequestRepository;

    public BookBorrowalService(BookBorrowalRepository bookBorrowalRepository, PersonRepository personRepository, BookRepository bookRepository, BookRequestRepository bookRequestRepository) {
        this.bookBorrowalRepository = bookBorrowalRepository;
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
        this.bookRequestRepository = bookRequestRepository;
    }

    public List<BookBorrowal> getAll() {
        return bookBorrowalRepository.findAll();
    }

    public Optional<BookBorrowal> getBookBorrowalById(Long id) {
        return bookBorrowalRepository.findById(id);
    }

    public void borrowBook(Long takerId, Long borrowerId, Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found."));

            if (book.getStatus() == Book.BookStatus.AVAILABLE) {

                BookRequest existingRequest = bookRequestRepository.findByRequesterIdAndBookId(borrowerId, bookId);

                if (existingRequest != null) {

                    book.setStatus(Book.BookStatus.ON_HAND);
                    bookRequestRepository.delete(existingRequest);
                } else {

                    int daysUntilExpiration = 5;

                    BookRequest request = new BookRequest();
                    request.setRequesterId(borrowerId);
                    request.setBookId(bookId);
                    request.setRequestDate(new Timestamp(System.currentTimeMillis()));
                    request.setExpirationDate(Timestamp.from(request.getRequestDate().toInstant().plus(daysUntilExpiration, ChronoUnit.DAYS)));

                    book.setStatus(Book.BookStatus.ON_HAND);
                    bookRequestRepository.save(request);
                }

                bookRepository.save(book);

                BookBorrowal borrowal = new BookBorrowal();
                borrowal.setBorrowerId(borrowerId);
                borrowal.setBookId(bookId);
                borrowal.setBorrowDate(new Timestamp(System.currentTimeMillis()));
                borrowal.setReturnDate(calculateReturnDate());

                updateBookStatus(borrowal);

                borrowal = bookBorrowalRepository.save(borrowal);
                log.info("Created a lease with ID: {}", borrowal.getId());
            } else if (book.getStatus() == Book.BookStatus.RESERVED) {

                BookRequest existingRequest = bookRequestRepository.findByRequesterIdAndBookId(takerId, bookId);

                if (existingRequest != null) {

                    book.setStatus(Book.BookStatus.ON_HAND);
                    bookRequestRepository.delete(existingRequest);

                    bookRepository.save(book);

                    BookBorrowal borrowal = new BookBorrowal();
                    borrowal.setBorrowerId(borrowerId);
                    borrowal.setBookId(bookId);
                    borrowal.setBorrowDate(new Timestamp(System.currentTimeMillis()));
                    borrowal.setReturnDate(calculateReturnDate());

                    updateBookStatus(borrowal);

                    borrowal = bookBorrowalRepository.save(borrowal);
                    log.info("Created a lease with ID: {}", borrowal.getId());
                } else {
                    throw new BookUnavailableException("The book with ID " + bookId + " is reserved by another user.");
                }
            } else {
                throw new BookUnavailableException("The book with ID " + bookId + " is not available for request.");
            }

        } catch (Exception e) {
            log.error("Error borrowing a book", e);
            throw e;
        }
    }

    public BookBorrowal returnBook(Long borrowalId) {

        Optional<BookBorrowal> optionalBorrowal = bookBorrowalRepository.findById(borrowalId);

        if (optionalBorrowal.isPresent()) {
            BookBorrowal borrowal = optionalBorrowal.get();
            borrowal.setReturnDate(Timestamp.valueOf(LocalDateTime.now()));

            updatePersonRating(borrowal);
            bookBorrowalRepository.save(borrowal);
            return borrowal;
        }

        throw new EntityNotFoundException("Sorry, rental not found " + borrowalId);
    }


    private void updatePersonRating(BookBorrowal borrowal) {
        Optional<Person> borrower = personRepository.findById(borrowal.getBorrowerId());
        int max = 10;
        int min = 0;

        if (borrower.isPresent()) {
            int currentRating = borrower.get().getRating();
            int ratingChange = calculateRatingChange(borrowal);

            int updatedRating = currentRating + ratingChange;

            if (updatedRating <= max && updatedRating >= min) {
                borrower.get().setRating(updatedRating);
                personRepository.updatePersonRating(updatedRating, borrower.get().getId());
                log.info("Updated user rating from ID: {} to: {}", borrower.get().getId(), updatedRating);
            }
        }
    }

    private void updateBookStatus(BookBorrowal borrowal) {
        Optional<Book> optionalBook = bookRepository.findById(borrowal.getBookId());

        if (optionalBook.isPresent()) {
            Book borrower = optionalBook.get();
            borrower.setStatus(Book.BookStatus.ON_HAND);
            bookRepository.save(borrower);
        } else {
            log.warn("Book with id {} not found.", borrowal.getBookId());
        }
    }

    private int calculateRatingChange(BookBorrowal borrowal) {
        int daysUse = 7;
        int during = 1;
        int overdue = 1;
        int withoutChanges = 0;
        if (borrowal.getReturnDate() != null) {
            long timeDifference = borrowal.getReturnDate().getTime() - borrowal.getBorrowDate().getTime();
            return timeDifference <= TimeUnit.DAYS.toMillis(daysUse) ? during : -overdue;
        }
        return withoutChanges;
    }

    private Timestamp calculateReturnDate() {
        int daysUse = 7;
        LocalDateTime returnDate = LocalDateTime.now().plusDays(daysUse);
        return Timestamp.valueOf(returnDate);
    }

    public void deleteBookBorrowalById(Long id) {
        bookBorrowalRepository.deleteById(id);
    }
}