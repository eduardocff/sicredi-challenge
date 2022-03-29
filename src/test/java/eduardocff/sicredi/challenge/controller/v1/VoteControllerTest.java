package eduardocff.sicredi.challenge.controller.v1;

import com.google.gson.Gson;
import eduardocff.sicredi.challenge.domain.Associate;
import eduardocff.sicredi.challenge.enums.UserInfoStatus;
import eduardocff.sicredi.challenge.enums.VoteStatus;
import eduardocff.sicredi.challenge.enums.VotingStatus;
import eduardocff.sicredi.challenge.exception.AssociateCanNotVoteException;
import eduardocff.sicredi.challenge.model.v1.*;
import eduardocff.sicredi.challenge.service.AssociateService;
import eduardocff.sicredi.challenge.service.VoteService;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class VoteControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private VoteController voteController;

    @Mock
    private VoteService voteService;

    @Mock
    private VotingControlService votingControlService;

    @Mock
    private AssociateService associateService;

    @Autowired
    private TestUtil testUtil;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(voteController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView()).build();
    }

    @Test
    public void newVote_ok() throws Exception {
        var voteDTO = new VoteDTO();
        voteDTO.setAssociateId(1L);
        voteDTO.setVoteStatus(VoteStatus.Sim);

        var votingDTO = new VotingDTO();
        votingDTO.setStatus(VotingStatus.OPEN.getStatus());
        votingDTO.setId(1L);
        votingDTO.setReason("Test reason");

        var associate = new Associate();
        associate.setCpf(59130224080L);
        associate.setId(1L);
        associate.setName("Edu");

        var associateDTO = new AssociateDTO();
        associateDTO.setCpf(59130224080L);
        associateDTO.setName("Edu");

        var userInfoStatus = new UserInfoStatusDTO();
        userInfoStatus.setStatus(UserInfoStatus.ABLE_TO_VOTE.getStatus());

        when(votingControlService.findVotingById(any(Long.class))).thenReturn(votingDTO);
        when(associateService.findAssociateById(any(Long.class))).thenReturn(associateDTO);
        when(associateService.checkAssociateCanVote(any(Long.class))).thenReturn(userInfoStatus);
        when(voteService.checkAlreadyVote(associateDTO.getCpf(), voteDTO)).thenReturn(Boolean.FALSE);

        Gson gson = new Gson();

        mockMvc.perform(
                post("/api/v1/vote/1")
                        .content(gson.toJson(voteDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void newVote_AssociateCanNotVoteException() throws Exception {
        var voteDTO = new VoteDTO();
        voteDTO.setAssociateId(1L);
        voteDTO.setVoteStatus(VoteStatus.Sim);

        var votingDTO = new VotingDTO();
        votingDTO.setStatus(VotingStatus.OPEN.getStatus());
        votingDTO.setId(1L);
        votingDTO.setReason("Test reason");

        var associateDTO = new AssociateDTO();
        associateDTO.setCpf(59130224080L);
        associateDTO.setName("Edu");

        when(votingControlService.findVotingById(any(Long.class))).thenReturn(votingDTO);
        when(associateService.findAssociateById(any(Long.class))).thenReturn(associateDTO);
        when(associateService.checkAssociateCanVote(any(Long.class))).thenThrow(new AssociateCanNotVoteException("teste"));

        Gson gson = new Gson();

        mockMvc.perform(
                post("/api/v1/vote/1")
                        .content(gson.toJson(voteDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    public void newVote_errorExternalAPI() throws Exception {
        var voteDTO = new VoteDTO();
        voteDTO.setAssociateId(1L);
        voteDTO.setVoteStatus(VoteStatus.Sim);

        var votingDTO = new VotingDTO();
        votingDTO.setStatus(VotingStatus.OPEN.getStatus());
        votingDTO.setId(1L);
        votingDTO.setReason("Test reason");

        var associateDTO = new AssociateDTO();
        associateDTO.setCpf(59130224080L);
        associateDTO.setName("Edu");

        when(votingControlService.findVotingById(any(Long.class))).thenReturn(votingDTO);
        when(associateService.findAssociateById(any(Long.class))).thenReturn(associateDTO);
        when(associateService.checkAssociateCanVote(any(Long.class))).thenThrow(HttpClientErrorException.class);

        Gson gson = new Gson();

        mockMvc.perform(
                post("/api/v1/vote/1")
                        .content(gson.toJson(voteDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
