package bookcrossing.controller;

import bookcrossing.domain.BookRequest;
import bookcrossing.service.BookRequestService;
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
        return bookRequest.map((ResponseEntity::ok))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<BookRequest> create(@RequestParam Long requesterId, @RequestParam Long bookId) {

        try {
            BookRequest createdRequest = bookRequestService.createBookRequest(requesterId, bookId);
            return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelBookRequest(@PathVariable Long id) {
        Optional<BookRequest> canceledRequest = bookRequestService.deleteBookRequestById(id);
        return canceledRequest.map(request -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}