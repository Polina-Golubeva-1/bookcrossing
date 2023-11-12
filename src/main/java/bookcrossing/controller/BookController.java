package bookcrossing.controller;

import bookcrossing.domain.Book;
import bookcrossing.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/book")
public class BookController {

    public final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        List<Book> resultList = bookService.getAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }


    @GetMapping("/findByName")
    public ResponseEntity<List<Book>> findAllByName(@RequestParam("name") String name) {
        List<Book> books = bookService.findAllByName(name);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.createBook(book) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping //TODO: @Valid BindingResult
    public ResponseEntity<HttpStatus> update(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.updateBook(book) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }


    @PutMapping("/{name}/{id}")
    public ResponseEntity<HttpStatus> updateName(@PathVariable String name, @PathVariable Long id) {
        bookService.updateName(name, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удаляет пользователя из бд",
            description = "Берет id, идет в бд и удаляет запись с этой id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Неверный ID"),
            @ApiResponse(responseCode = "404", description = "id не найдено"),
            @ApiResponse(responseCode = "204", description = "Значит что успешно удалили")})
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "ID of the person to be deleted") @PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
