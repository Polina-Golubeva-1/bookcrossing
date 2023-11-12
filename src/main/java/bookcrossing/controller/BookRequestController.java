package bookcrossing.controller;

import bookcrossing.domain.BookRequest;
import bookcrossing.service.BookRequestService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/book_request")
public class BookRequestController {
    public final BookRequestService bookRequestService;

    public BookRequestController(BookRequestService bookRequestService) {
        this.bookRequestService = bookRequestService;
    }
    @GetMapping
    public ResponseEntity<List<BookRequest>> getAll() {
        List<BookRequest> resultList = bookRequestService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequest> getBookRequestById(@PathVariable("id") Long id) {
        Optional<BookRequest> bookRequest = bookRequestService.getBookRequestById(id);
        return bookRequest.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam Long requesterId, @RequestParam Long bookId) {
        //TODO: security -> login -> id
        //TODO: Optional
        BookRequest request = bookRequestService.createBookRequest(requesterId, bookId);

        if (request != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "ID of the person to be deleted") @PathVariable("id") Long id) {
        bookRequestService.deleteBookRequestById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}