package bookcrossing.service;

import bookcrossing.domain.BlackList;
import bookcrossing.domain.Person;
import bookcrossing.repository.BlackListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlackListServiceTest {

    @Mock
    private BlackListRepository blackListRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private BlackListService blackListService;

    @Test
    void getAll_ShouldReturnListOfBlackListEntries() {

        List<BlackList> blackListEntries = new ArrayList<>();
        when(blackListRepository.findAll()).thenReturn(blackListEntries);

        List<BlackList> result = blackListService.getAll();

        assertEquals(blackListEntries, result);
    }

    @Test
    void findByPersonId_ShouldReturnBlackListEntry_WhenPersonIdExists() {

        Long personId = 1L;
        BlackList blackListEntry = new BlackList();
        when(blackListRepository.findByPersonId(personId)).thenReturn(Optional.of(blackListEntry));

        Optional<BlackList> result = blackListService.findByPersonId(personId);

        assertTrue(result.isPresent());
        assertEquals(blackListEntry, result.get());
    }

    @Test
    void findByPersonId_ShouldReturnEmpty_WhenPersonIdDoesNotExist() {

        Long personId = 1L;
        when(blackListRepository.findByPersonId(personId)).thenReturn(Optional.empty());

        Optional<BlackList> result = blackListService.findByPersonId(personId);

        assertTrue(result.isEmpty());
    }

    @Test
    void addToBlackList_ShouldAddToBlackList_WhenPersonIsNotInBlackListAndRatingIsZero() {

        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);
        person.setFirstName("Petr");
        person.setRating(0);

        when(personService.getPersonById(personId)).thenReturn(Optional.of(person));
        when(blackListRepository.save(any())).thenReturn(new BlackList());

        Optional<BlackList> result = blackListService.addToBlackList(personId);

        assertTrue(result.isPresent());
        verify(blackListRepository, times(1)).save(any());
    }

    @Test
    void addToBlackList_ShouldReturnEmpty_WhenPersonIsInBlackList() {

        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);
        person.setFirstName("Petr");
        person.setRating(0);

        when(personService.getPersonById(personId)).thenReturn(Optional.of(person));
        when(blackListRepository.findByPersonId(personId)).thenReturn(Optional.of(new BlackList()));

        Optional<BlackList> result = blackListService.addToBlackList(personId);

        System.out.println("Result: " + result);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "The result should be an empty Optional");
    }

    @Test
    void addToBlackList_ShouldReturnEmpty_WhenPersonRatingIsNotZero() {

        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);
        person.setFirstName("John");
        person.setRating(1);

        when(personService.getPersonById(personId)).thenReturn(Optional.of(person));

        Optional<BlackList> result = blackListService.addToBlackList(personId);

        assertTrue(result.isEmpty());
        verify(blackListRepository, never()).save(any());
    }

    @Test
    void deleteBlackListById_ShouldDeleteBlackListEntry() {

        Long blackListId = 1L;
        doNothing().when(blackListRepository).deleteById(blackListId);

        blackListService.deleteBlackListById(blackListId);

        verify(blackListRepository, times(1)).deleteById(blackListId);
    }

    @Test
    void isPersonNotInBlackList_ShouldReturnTrue_WhenPersonIsNotInBlackList() {

        Long personId = 1L;
        when(blackListRepository.findByPersonId(personId)).thenReturn(Optional.empty());

        boolean result = blackListService.isPersonNotInBlackList(personId);

        assertTrue(result);
    }

    @Test
    void isPersonNotInBlackList_ShouldReturnFalse_WhenPersonIsInBlackList() {

        Long personId = 1L;
        when(blackListRepository.findByPersonId(personId)).thenReturn(Optional.of(new BlackList()));

        boolean result= blackListService.isPersonNotInBlackList(personId);

        assertFalse(result);
    }
}