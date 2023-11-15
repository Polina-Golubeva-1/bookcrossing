package bookcrossing.controller;

import bookcrossing.domain.BookBorrowal;
import bookcrossing.service.BookBorrowalService;
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

    public BookBorrowalController(BookBorrowalService bookBorrowalService) {
        this.bookBorrowalService = bookBorrowalService;
    }

    @GetMapping
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

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam Long borrowerId, @RequestParam Long bookId) {
        BookBorrowal borrowal = bookBorrowalService.createBookBorrowal(borrowerId, bookId);

        if (borrowal != null) {
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

    @DeleteMapping("/{id}")
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