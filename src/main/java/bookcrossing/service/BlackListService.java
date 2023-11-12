package bookcrossing.service;

import bookcrossing.domain.BlackList;
import bookcrossing.repository.BlackListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BlackListService {

    private final BlackListRepository blackListRepository;

    @Autowired
    public BlackListService(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    public List<BlackList> getAll() {
        return blackListRepository.findAll();
    }

    public Optional<BlackList> getBlackListById(Long id) {
        return blackListRepository.findById(id);
    }

    public BlackList addToBlackList(BlackList blackList) {
        return blackListRepository.save(blackList);
    }

    public List<BlackList> getBlackListByRating(Integer rating) {
        return blackListRepository.findByRating(rating);
    }

    public List<BlackList> getBlackListByPersonId(Long personId) {
        return blackListRepository.findByPersonId(personId);
    }

    public void deleteBlackListById(Long id) {
        blackListRepository.deleteById(id);
    }
}