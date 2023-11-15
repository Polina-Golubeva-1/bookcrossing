package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookBorrowal;
import bookcrossing.domain.Person;
import bookcrossing.repository.BookBorrowalRepository;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
        borrowal.setBorrowData(new Timestamp(System.currentTimeMillis()));

        updateBookStatus(borrowal);

        borrowal = bookBorrowalRepository.save(borrowal);
        log.info("Created a lease with ID: {}", borrowal.getId());

        return borrowal;
    }

    public BookBorrowal returnBook(Long borrowalId) {
        BookBorrowal borrowal = bookBorrowalRepository.findById(borrowalId).orElse(null);

        if (borrowal != null) {
            borrowal.setReturnData(new Timestamp(System.currentTimeMillis()));
            borrowal = bookBorrowalRepository.save(borrowal);

            updatePersonRating(borrowal);

            return borrowal;
        }

        return null; //TODO: don't return null
    }

    private void updatePersonRating(BookBorrowal borrowal) {
        Optional<Person> borrower = personRepository.findById(borrowal.getBorrowerId());

        if (borrower.isPresent()) {
            int currentRating = borrower.get().getRating();
            int ratingChange = calculateRatingChange(borrowal);

            int updatedRating = currentRating + ratingChange;

            if (updatedRating <= 10 && updatedRating >= 0) {
                borrower.get().setRating(updatedRating);
                personRepository.updatePersonRating(updatedRating, borrower.get().getId());
                log.info("Updated user rating from ID: {} to: {}", borrower.get().getId(), updatedRating);
            }
        }
    }

    private void  updateBookStatus(BookBorrowal borrowal) {
        Optional<Book> optionalBook = bookRepository.findById(borrowal.getBorrowerId());

        if (optionalBook.isPresent()) {
            Book borrower = optionalBook.get();
            borrower.setStatus(Book.BookStatus.ON_HAND);
            bookRepository.save(borrower);
        }
            }

    private int calculateRatingChange(BookBorrowal borrowal) {
        if (borrowal.getReturnData() != null) {
            long timeDifference = borrowal.getReturnData().getTime() - borrowal.getBorrowData().getTime();
            return timeDifference <= TimeUnit.DAYS.toMillis(7)?1:-1;
        }
        return 0;
    }

    public void deleteBookBorrowalById(Long id) {
        bookBorrowalRepository.deleteById(id);
    }
}