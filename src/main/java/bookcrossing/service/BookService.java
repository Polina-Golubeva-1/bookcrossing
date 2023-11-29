package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.exeption_resolver.BookNotFoundException;
import bookcrossing.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {

        private final BookRepository bookRepository;

        public BookService(BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }

        public List<Book> getAll() {
            return bookRepository.findAll();
        }

        public Book getBookById(Long id) {
            Optional<Book> book = bookRepository.findById(id);
            return book.orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        }

        public List<Book> getAllAvailableBooks() {
            return bookRepository.findByStatus(Book.BookStatus.AVAILABLE);
        }

        public Boolean createBook(Book book) {
            try {

                book.setCreated(Timestamp.valueOf(LocalDateTime.now()));
                bookRepository.save(book);
                log.info(String.format("Book with  name %s created!", book.getName()));
            } catch (Exception e) {
                log.warn(String.format("Book with name %s have error! %s", book.getName(), e));
                return false;
            }
            return true;
        }

    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(existingBook -> {

                    existingBook.setName(updatedBook.getName());
                    existingBook.setGenre(updatedBook.getGenre());
                    existingBook.setAuthor(updatedBook.getAuthor());

                    try {
                        Book updated = bookRepository.save(existingBook);
                        log.info("User with ID {} updated!", updated.getId());
                        return updated;
                    } catch (Exception e) {
                        log.warn("Error updating person", e);
                        throw new RuntimeException("Error updating person", e);
                    }
                });
    }

        public Optional<Book> deleteBookById(Long id) {
        Optional<Book> bookToDelete = bookRepository.findById(id);
        bookToDelete.ifPresent(book -> bookRepository.deleteById(id));
        return bookToDelete;
        }

       public List<Book> findAllByName(String name){
        return bookRepository.findAllByName(name);
         }
       }

