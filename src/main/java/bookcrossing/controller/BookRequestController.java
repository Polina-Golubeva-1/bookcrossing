package bookcrossing.controller;

import bookcrossing.domain.BookRent;
import bookcrossing.service.BlackListService;
import bookcrossing.service.BookRentService;
import bookcrossing.service.BookService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book_request")
public class BookRequestController {
    public final BookRentService bookRentService;
    private final BlackListService blackListService;
    private final BookService bookService;

    public BookRequestController(BookRentService bookRentService, BlackListService blackListService, BookService bookService) {
        this.bookRentService = bookRentService;
        this.blackListService = blackListService;
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookRent>> getAll() {
        List<BookRent> resultList = bookRentService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRent> getBookRequestById(@PathVariable("id") Long id) {
        Optional<BookRent> bookRequest = bookRentService.getBookRequestById(id);
        return bookRequest.map((ResponseEntity::ok))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/rent")
    public ResponseEntity<BookRent> rentBook(@RequestParam Long requesterId, @RequestParam Long bookId) {
        if (blackListService.isPersonNotInBlackList(requesterId) && !bookService.isPersonOwnsBook(requesterId,bookId)) {
            bookRentService.rentBook(requesterId, bookId);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelBookRequest(@PathVariable Long id) {
        Optional<BookRent> canceledRequest = bookRentService.deleteBookRequestById(id);
        return canceledRequest.map(request -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}