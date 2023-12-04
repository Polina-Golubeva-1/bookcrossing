package bookcrossing.repository;

import bookcrossing.domain.BlackList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BlackListRepositoryTest {

    @Autowired
    private BlackListRepository blackListRepository;

    @Test
    void findByPersonId_ShouldReturnBlackList_WhenPersonIdExists() {

        BlackList blackList = new BlackList();
        blackList.setPersonId(1L);
        blackList.setPersonName("TestUserName");
        blackListRepository.save(blackList);

        Optional<BlackList> result = blackListRepository.findByPersonId(1L);

        assertTrue(result.isPresent());
        assertThat(result.get().getPersonId()).isEqualTo(1L);
    }
    @Test
    void findByPersonId_ShouldReturnEmpty_WhenPersonIdDoesNotExist() {

        Optional<BlackList> result = blackListRepository.findByPersonId(999L);

        assertTrue(result.isEmpty());
    }
}