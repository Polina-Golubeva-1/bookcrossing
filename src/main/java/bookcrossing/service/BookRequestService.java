package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.domain.BookRequest;
import bookcrossing.repository.BookRepository;
import bookcrossing.repository.BookRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  /*  public BookRequest createBookRequest(Long requesterId, Long bookId) {
        BookRequest request = new BookRequest();
        request.setRequesterId(requesterId);
        request.setBookId(bookId);
        request.setRequestDate(new Timestamp(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(request.getRequestDate().getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Timestamp expirationDate = new Timestamp(calendar.getTimeInMillis());
        request.setExpirationDate(expirationDate);

        updateBookStatus(request);
        request = bookRequestRepository.save(request);
        return request;
    }
*/
 /*   public void cancelExpiredReservations() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Optional<Book> expiredRequests = bookRequestRepository.findByExpirationDateBeforeAndStatusIsNull(now);

        for (BookRequest request : expiredRequests) {
            request.setStatus(Book.BookStatus.AVAILABLE);
            bookRequestRepository.save(request);
        }
    }*/
    private void  updateBookStatus(BookRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(request.getRequesterId());

        if (optionalBook.isPresent()) {
            Book bookRequest = optionalBook.get();
            bookRequest.setStatus(Book.BookStatus.RESERVED);
            bookRepository.save(bookRequest);
        }
    }

    public Optional<BookRequest> deleteBookRequestById(Long id) {
        Optional<BookRequest> bookRequestToDelete = bookRequestRepository.findById(id);
        bookRequestToDelete.ifPresent(bookRequest -> bookRequestRepository.deleteById(id));
        return bookRequestToDelete;
    }
}
