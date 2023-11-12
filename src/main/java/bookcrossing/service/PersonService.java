package bookcrossing.service;

import bookcrossing.domain.Person;
import bookcrossing.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
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

    public Boolean createPerson(String firstName, String secondName, Integer age, Integer phone, String email, String address) {
        Person person = new Person();
        try {
            person.setFirstName(firstName);
            person.setSecondName(secondName);
            person.setAge(age);
            person.setPhone(phone);
            person.setEmail(email);
            person.setCreated(new Timestamp(System.currentTimeMillis()));
            person.setRating(10);
            person.setAddress(address);

            person = personRepository.save(person);
            log.info("Person with ID {} created!", person.getFirstName());
        } catch (Exception e) {
            log.warn(String.format("Person with first name %s have error! %s", person.getFirstName(), e));
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
                        log.error("Error updating person", e);
                        throw new RuntimeException("Error updating person", e);
                    }
                });
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

    public List<Person> findAllByAddress(String address){
        return personRepository.findAllByAddress(address);
    }
}