package bookcrossing.exeption_resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionResolver {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<HttpStatus> exceptionHandlerMethodForRuntimeException(Exception e) {
        log.warn(String.valueOf(e));
        return  new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
