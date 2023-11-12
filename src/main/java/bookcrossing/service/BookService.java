package bookcrossing.service;

import bookcrossing.domain.Book;
import bookcrossing.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        public Optional<Book> getBookById(Long id) {
            return bookRepository.findById(id);
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

        public Boolean updateBook(Book book) {
            try {
                bookRepository.saveAndFlush(book);
                log.info(String.format("Book with id %s updated!", book.getId()));
            } catch (Exception e) {
                log.warn(String.format("Book with id %s have error! %s", book.getId(), e));
                return false;
            }
            return true;
        }

        public void deleteBookById(Long id) {
            bookRepository.deleteById(id);
        }



        @Transactional(rollbackFor = Exception.class)
        public void updateName(String name, long id){
            bookRepository.updateNameById(name,id);
        }




    public List<Book> findAllByName(String name){
        return bookRepository.findAllByName(name);
    }
}

