package bookcrossing.controller;

import bookcrossing.domain.BookRequest;
import bookcrossing.service.BlackListService;
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
    private final BlackListService blackListService;

    public BookRequestController(BookRequestService bookRequestService, BlackListService blackListService) {
        this.bookRequestService = bookRequestService;
        this.blackListService = blackListService;
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

    @PostMapping("/rent")
    public ResponseEntity<BookRequest> rentBook(@RequestParam Long requesterId, @RequestParam Long bookId) {
        if (blackListService.canRentBook(requesterId)) {
            bookRequestService.rentBook(requesterId, bookId);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }



    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelBookRequest(@PathVariable Long id) {
        Optional<BookRequest> canceledRequest = bookRequestService.deleteBookRequestById(id);
        return canceledRequest.map(request -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}