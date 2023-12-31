package bookcrossing.controller;

import bookcrossing.domain.BookBorrowal;
import bookcrossing.service.BlackListService;
import bookcrossing.service.BookBorrowalService;
import bookcrossing.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book_borrowal")
public class BookBorrowalController {
    public final BookBorrowalService bookBorrowalService;
    private final BlackListService blackListService;
    private final BookService bookService;

    public BookBorrowalController(BookBorrowalService bookBorrowalService, BlackListService blackListService, BookService bookService) {
        this.bookBorrowalService = bookBorrowalService;
        this.blackListService = blackListService;
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookBorrowal>> getAll() {
        Optional<List<BookBorrowal>> resultList = Optional.ofNullable(bookBorrowalService.getAll());
        return resultList.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookBorrowal> getBookBorrowalById(@PathVariable("id") Long id) {
        Optional<BookBorrowal> bookBorrowal = bookBorrowalService.getBookBorrowalById(id);
        return bookBorrowal.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/borrow")
    public ResponseEntity<HttpStatus> borrowBook(@RequestParam Long borrowerId, @RequestParam Long bookId, @RequestParam(value = "takerId", required = false) Long takerId) {
        if (blackListService.isPersonNotInBlackList(borrowerId) && !bookService.isPersonOwnsBook(borrowerId, bookId)) {
            bookBorrowalService.borrowBook(takerId, borrowerId, bookId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<BookBorrowal> returnBook(@RequestParam Long borrowalId) {
        Optional<BookBorrowal> borrowalOptional = Optional.ofNullable(bookBorrowalService.returnBook(borrowalId));

        return borrowalOptional.map(borrowal -> new ResponseEntity<>(borrowal, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "ID of the person to be deleted") @PathVariable("id") Long id) {
        Optional<BookBorrowal> bookBorrowalToDelete = bookBorrowalService.getBookBorrowalById(id);

        if (bookBorrowalToDelete.isPresent()) {
            bookBorrowalService.deleteBookBorrowalById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}