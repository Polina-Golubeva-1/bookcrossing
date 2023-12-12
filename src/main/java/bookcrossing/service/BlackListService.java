package bookcrossing.service;

import bookcrossing.domain.BlackList;
import bookcrossing.domain.Person;
import bookcrossing.repository.BlackListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final PersonService personService;

    public BlackListService(BlackListRepository blackListRepository, PersonService personService) {
        this.blackListRepository = blackListRepository;
        this.personService = personService;
    }

    public List<BlackList> getAll() {
        return blackListRepository.findAll();
    }

    public Optional<BlackList> findByPersonId(Long personId) {
        return blackListRepository.findByPersonId(personId);
    }

    public Optional<BlackList> addToBlackList(Long personId) {
        Optional<Person> person = personService.getPersonById(personId);

        if (person.isPresent() && person.get().getRating() == 0) {

            Optional<BlackList> existingEntry = blackListRepository.findByPersonId(personId);
            if (existingEntry.isPresent()) {
                return Optional.empty();
            }

            BlackList blackListEntry = new BlackList();
            blackListEntry.setPersonId(personId);
            blackListEntry.setPersonName(person.get().getFirstName());

            blackListRepository.save(blackListEntry);
            return Optional.of(blackListEntry);
        } else {
            return Optional.empty();
        }
    }

    public void deleteBlackListById(Long id) {
        blackListRepository.deleteById(id);
    }

    public boolean isPersonNotInBlackList(Long personId) {
        Optional<BlackList> blackListEntry = blackListRepository.findByPersonId(personId);
        return blackListEntry.isEmpty();
    }


}