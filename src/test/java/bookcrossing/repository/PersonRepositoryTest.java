package bookcrossing.repository;

import bookcrossing.domain.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository userRepository;
    static Person userInfo;

    @BeforeAll
    static void beforeAll() {
        userInfo = new Person();
        userInfo.setFirstName("TestFirstName");
        userInfo.setSecondName("TestLastName");
        userInfo.setAge(20);
        userInfo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void findAllTest() {
        userRepository.save(userInfo);
        List<Person> newList = userRepository.findAll();
        Assertions.assertNotNull(newList);
    }

    @Test
    void findByIdTest() {
        Person saved = userRepository.save(userInfo);
        Optional<Person> newUser = userRepository.findById(saved.getId());
        Assertions.assertTrue(newUser.isPresent());
    }

    @Test
    void saveTest() {
        List<Person> oldList = userRepository.findAll();
        userRepository.save(userInfo);
        List<Person> newList = userRepository.findAll();
        Assertions.assertNotEquals(oldList.size(), newList.size());
    }

    @Test
    void updateTest() {
        Person userSaved = userRepository.save(userInfo);
        userSaved.setFirstName("UPDATED_NAME");
        Person userUpdated = userRepository.saveAndFlush(userSaved);
        Assertions.assertEquals(userUpdated.getFirstName(), "UPDATED_NAME");
    }

    @Test
    void deleteTest() {
        Person userSaved = userRepository.save(userInfo);
        userRepository.delete(userSaved);
        Optional<Person> user = userRepository.findById(userSaved.getId());
        Assertions.assertFalse(user.isPresent());
    }
}