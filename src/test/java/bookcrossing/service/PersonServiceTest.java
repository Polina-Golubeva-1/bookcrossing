package bookcrossing.service;

import bookcrossing.domain.Person;
import bookcrossing.repository.PersonRepository;
import bookcrossing.security.service.SecurityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    SecurityService securityService;

    static List<Person> personList = null;
    static Person person = null;

    static Long personId = 10L;

    @BeforeAll
    static void beforeAll() {
        personList = new ArrayList<>();
        person = new Person();
        person.setId(personId);
        person.setFirstName("Anya");
        person.setSecondName("Pankova");

        personList.add(person);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getPersonByIdTest() {
        Mockito.when(securityService.checkAccessById(anyLong())).thenReturn(true);
        Mockito.when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        Optional<Person> result = personService.getPersonById(personId);
        Mockito.verify(personRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.get());
    }

    @Test
    void getAllTest() {
        Mockito.when(personRepository.findAll()).thenReturn(personList);

        List<Person> resultList = personService.getAll();
        Mockito.verify(personRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

 /*   @Test
    void createTest() {
        Mockito.when(personRepository.save(any())).thenReturn(person);

        Boolean result = personService.createPerson(person);
        Mockito.verify(personRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }*/
/*

    @Test
    void updateTest() {
        Mockito.when(personRepository.saveAndFlush(any())).thenReturn(person);

        Boolean result = personService.updatePerson(person);
        Mockito.verify(personRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
    }
*/
    @Test
    void deleteTest() {
        personService.deletePersonById(10L);
        Mockito.verify(personRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
