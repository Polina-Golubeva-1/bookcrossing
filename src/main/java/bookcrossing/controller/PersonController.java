package bookcrossing.controller;


import bookcrossing.domain.Person;
import bookcrossing.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
@RequestMapping("/person")
public class PersonController {
    public final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAll() {
        Optional<List<Person>> optionalResult = Optional.ofNullable(personService.getAll());

        return optionalResult.map(people -> new ResponseEntity<>(people, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") Long id) {
        Optional<Person> person = personService.getPersonById(id);
        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

  /*  @GetMapping("/findByAddress")
    public ResponseEntity<List<Person>> findAllByAddress(@RequestParam("address") String address) {
        List<Person> persons = personService.findAllByAddress(address);
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }*/

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam String firstName,@RequestParam String secondName,@RequestParam Integer age,@RequestParam Integer phone,@RequestParam String email,@RequestParam String address) {
        return new ResponseEntity<> (personService.createPerson(firstName, secondName, age, phone, email, address) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id,@Valid @RequestBody   Person updatedPerson) {
        Optional<Person> updated = personService.updatePerson(id, updatedPerson);

        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Удаляет пользователя из бд",
            description = "Берет id, идет в бд и удаляет запись с этой id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Неверный ID"),
            @ApiResponse(responseCode = "404", description = "id не найдено"),
            @ApiResponse(responseCode = "204", description = "Значит что успешно удалили")})
//TODO: swagger
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@Parameter(description = "ID of the person to be deleted") @PathVariable("id") Long id) {
        personService.deletePersonById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
