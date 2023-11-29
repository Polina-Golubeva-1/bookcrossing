package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookBorrowal;
import bookcrossing.domain.Person;
import bookcrossing.repository.BookBorrowalRepository;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BookBorrowalService {

    private final BookBorrowalRepository bookBorrowalRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    public BookBorrowalService(BookBorrowalRepository bookBorrowalRepository, PersonRepository personRepository, BookRepository bookRepository) {
        this.bookBorrowalRepository = bookBorrowalRepository;
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    public List<BookBorrowal> getAll() {
        return bookBorrowalRepository.findAll();
    }

    public Optional<BookBorrowal> getBookBorrowalById(Long id) {
        return bookBorrowalRepository.findById(id);
    }

    public BookBorrowal createBookBorrowal(Long borrowerId, Long bookId) {

        BookBorrowal borrowal = new BookBorrowal();
        borrowal.setBorrowerId(borrowerId);
        borrowal.setBookId(bookId);
        borrowal.setBorrowDate(new Timestamp(System.currentTimeMillis()));
        borrowal.setReturnDate(calculateReturnDate());

        updateBookStatus(borrowal);

        borrowal = bookBorrowalRepository.save(borrowal);
        log.info("Created a lease with ID: {}", borrowal.getId());

        return borrowal;
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

    private void  updateBookStatus(BookBorrowal borrowal) {
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
            return timeDifference <= TimeUnit.DAYS.toMillis(daysUse)?during:-overdue;
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