package bookcrossing.controller;

import bookcrossing.domain.BlackList;
import bookcrossing.service.BlackListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BlackListControllerTest {

    @Mock
    private BlackListService blackListService;

    @InjectMocks
    private BlackListController blackListController;

    private MockMvc mockMvc;

    private BlackList sampleBlackList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blackListController).build();

        sampleBlackList = new BlackList();
        sampleBlackList.setId(1L);
        sampleBlackList.setPersonName("Mila");
        sampleBlackList.setPersonId(123L);
    }

    @Test
    void getAll_ReturnsListOfBlackListEntries() throws Exception {
        when(blackListService.getAll()).thenReturn(Collections.singletonList(sampleBlackList));

        mockMvc.perform(MockMvcRequestBuilders.get("/black_list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleBlackList.getId()))
                .andExpect(jsonPath("$[0].personName").value(sampleBlackList.getPersonName()))
                .andExpect(jsonPath("$[0].personId").value(sampleBlackList.getPersonId()));
    }

    @Test
    void getAll_ReturnsNotFoundIfNoEntries() throws Exception {
        when(blackListService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/black_list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findByPersonId_ReturnsBlackListEntryIfExists() throws Exception {
        Long personId = 1L;
        when(blackListService.findByPersonId(personId)).thenReturn(Optional.of(sampleBlackList));

        mockMvc.perform(MockMvcRequestBuilders.get("/black_list/findByPersonId/{personId}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleBlackList.getId()))
                .andExpect(jsonPath("$.personName").value(sampleBlackList.getPersonName()))
                .andExpect(jsonPath("$.personId").value(sampleBlackList.getPersonId()));
    }

    @Test
    void findByPersonId_ReturnsNotFoundIfNotExists() throws Exception {
        Long personId = 1L;
        when(blackListService.findByPersonId(personId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/black_list/findByPersonId/{personId}", personId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addToBlackList_ReturnsCreatedStatusIfAdded() throws Exception {
        Long personId = 1L;
        when(blackListService.addToBlackList(personId)).thenReturn(Optional.of(sampleBlackList));

        mockMvc.perform(MockMvcRequestBuilders.post("/black_list")
                        .param("personId", personId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleBlackList.getId()))
                .andExpect(jsonPath("$.personName").value(sampleBlackList.getPersonName()))
                .andExpect(jsonPath("$.personId").value(sampleBlackList.getPersonId()));
    }

    @Test
    void addToBlackList_ReturnsNotFoundStatusIfNotAdded() throws Exception {
        Long personId = 1L;
        when(blackListService.addToBlackList(personId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/black_list")
                        .param("personId", personId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ReturnsNoContentStatus() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/black_list/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}