package bookcrossing.service;

import bookcrossing.domain.Person;
import bookcrossing.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public Boolean createPerson(Person newPerson) {
        try {

            newPerson.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            personRepository.save(newPerson);
            log.info(String.format("Book with  name %s created!", newPerson.getFirstName()));
        } catch (Exception e) {
            log.warn(String.format("Book with name %s have error! %s", newPerson.getFirstName(), e));
            return false;
        }
        return true;
    }

    public Optional<Person> updatePerson(Long id, Person updatedPerson) {
        return personRepository.findById(id)
                .map(existingPerson -> {

                    existingPerson.setFirstName(updatedPerson.getFirstName());
                    existingPerson.setSecondName(updatedPerson.getSecondName());
                    existingPerson.setAge(updatedPerson.getAge());
                    existingPerson.setPhone(updatedPerson.getPhone());
                    existingPerson.setEmail(updatedPerson.getEmail());
                    existingPerson.setAddress(updatedPerson.getAddress());

                    try {
                        Person updated = personRepository.save(existingPerson);
                        log.info("User with ID {} updated!", updated.getId());
                        return updated;
                    } catch (Exception e) {
                        log.warn("Error updating person", e);
                        throw new RuntimeException("Error updating person", e);
                    }
                });
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

}