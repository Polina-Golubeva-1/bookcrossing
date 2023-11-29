package bookcrossing.exeption_resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HttpStatus> exceptionHandlerMethodForRuntimeException(Exception e) {
        log.warn(String.valueOf(e));
        return  new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<String> handleBookUnavailableException(BookUnavailableException e) {
        log.warn("Book Unavailable Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException e) {
        log.warn("Book Not Found Exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
