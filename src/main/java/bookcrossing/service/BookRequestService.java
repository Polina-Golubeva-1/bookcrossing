package bookcrossing.service;

import bookcrossing.domain.BookRequest;
import bookcrossing.repository.BookRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class BookRequestService {

   private final BookRequestRepository bookRequestRepository;


    public BookRequestService(BookRequestRepository bookRequestRepository) {
        this.bookRequestRepository = bookRequestRepository;
    }

    public List<BookRequest> getAll() {
        return bookRequestRepository.findAll();
    }

    public Optional<BookRequest> getBookRequestById(Long id) {
        return bookRequestRepository.findById(id);
    }

    public BookRequest createBookRequest(Long requesterId, Long bookId) {
        BookRequest request = new BookRequest();
        request.setRequesterId(requesterId);
        request.setBookId(bookId);
        request.setRequestDate(new Timestamp(System.currentTimeMillis()));

        return bookRequestRepository.save(request);
    }

    public void deleteBookRequestById(Long id) {
        bookRequestRepository.deleteById(id);
    }
}
