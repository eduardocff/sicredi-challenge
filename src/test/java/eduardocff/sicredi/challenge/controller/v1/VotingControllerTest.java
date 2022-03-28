package eduardocff.sicredi.challenge.controller.v1;

import com.google.gson.Gson;
import eduardocff.sicredi.challenge.enums.VotingStatus;
import eduardocff.sicredi.challenge.exception.EntityNotFoundException;
import eduardocff.sicredi.challenge.model.v1.VotingDTO;
import eduardocff.sicredi.challenge.service.VotingControlService;
import eduardocff.sicredi.challenge.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class VotingControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private VotingController votingController;

    @Mock
    private VotingControlService votingControlService;

    @Autowired
    private TestUtil testUtil;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(votingController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView()).build();
    }

    @Test
    public void createNewVoting_ok() throws Exception {
        var votingDTO = new VotingDTO();
        votingDTO.setReason("Teste Reason");

        var returnVotingDTO = new VotingDTO();
        returnVotingDTO.setReason(votingDTO.getReason());
        returnVotingDTO.setId(1L);
        returnVotingDTO.setStatus(VotingStatus.CREATED.getStatus());

        Gson gson = new Gson();

        when(votingControlService.createVoting(votingDTO)).thenReturn(returnVotingDTO);

        mockMvc.perform(
                post("/api/v1/voting")
                        .content(gson.toJson(votingDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createNewVoting_invalidReason() throws Exception {
        var votingDTO = new VotingDTO();
        votingDTO.setReason(null);

        Gson gson = new Gson();

        when(votingControlService.createVoting(votingDTO)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(
                post("/api/v1/voting")
                        .content(gson.toJson(votingDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void startVoting_ok() throws Exception {
        var votingDTO = new VotingDTO();
        votingDTO.setStatus(VotingStatus.CREATED.getStatus());
        votingDTO.setId(1L);
        votingDTO.setReason("Test reason");

        when(votingControlService.findVotingById(any(Long.class))).thenReturn(votingDTO);
        when(votingControlService.openNewVoting(any(Long.class))).thenReturn(1);

        Gson gson = new Gson();

        mockMvc.perform(
                patch("/api/v1/voting/start/1")
                        .content(gson.toJson(votingDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void startVoting_EntityNotFound() throws Exception {
        var votingDTO = new VotingDTO();
        votingDTO.setStatus(VotingStatus.CREATED.getStatus());
        votingDTO.setId(1L);
        votingDTO.setReason("Test reason");

        when(votingControlService.findVotingById(any(Long.class))).thenThrow(new EntityNotFoundException(any(String.class)));

        Gson gson = new Gson();

        mockMvc.perform(
                patch("/api/v1/voting/start/1")
                        .content(gson.toJson(votingDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
