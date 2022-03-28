package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.domain.Voting;
import eduardocff.sicredi.challenge.enums.VotingStatus;
import eduardocff.sicredi.challenge.exception.VotingAlreadyOpenedException;
import eduardocff.sicredi.challenge.model.v1.VotingDTO;
import eduardocff.sicredi.challenge.repository.VotingRepository;
import eduardocff.sicredi.challenge.utils.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
public class VotingControlServiceTest {

    @InjectMocks
    private VotingControlService votingControlService;

    @Mock
    private VotingRepository votingRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void createVoting_ok() {
        var votingDTO = new VotingDTO();
        votingDTO.setReason("Test Reason");

        var createdVotingDTO = new Voting();
        createdVotingDTO.setReason("Test Reason");
        createdVotingDTO.setId(1L);
        createdVotingDTO.setStatus(VotingStatus.CREATED.getStatus());

        when(votingRepository.save(any(Voting.class))).thenReturn(createdVotingDTO);

        var savedDTO = votingControlService.createVoting(votingDTO);

        verify(votingRepository, times(1)).save(any(Voting.class));
        Assertions.assertEquals(votingDTO.getReason(), savedDTO.getReason());
    }

    @Test
    public void createVoting_WithoutReason_error() {
        var votingDTO = new VotingDTO();
        votingDTO.setReason(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> votingControlService.createVoting(votingDTO));
    }

    @Test
    public void findVotingById_ok() {
        Voting votingSaved = new Voting();
        votingSaved.setId(1L);
        votingSaved.setReason("Test reason");
        votingSaved.setStatus(VotingStatus.CREATED.getStatus());

        when(votingRepository.findById(votingSaved.getId())).thenReturn(Optional.of(votingSaved));

        VotingDTO votingDTO = votingControlService.findVotingById(votingSaved.getId());

        verify(votingRepository, times(1)).findById(any(Long.class));
        Assertions.assertEquals(votingDTO.getReason(), votingSaved.getReason());
        Assertions.assertEquals(votingDTO.getId(), votingSaved.getId());
        Assertions.assertEquals(votingDTO.getStatus(), votingSaved.getStatus());
    }

    @Test
    public void checkVotingStatusToOpen_exception() {
        var votingDTO = new VotingDTO();
        votingDTO.setReason("Test Reason");
        votingDTO.setStatus(VotingStatus.CLOSED.getStatus());

        Assertions.assertThrows(VotingAlreadyOpenedException.class, () -> votingControlService.checkVotingStatusToOpen(votingDTO));
    }

    @Test
    public void openNewVoting_ok() throws Exception {
        Long updatebleVoting = 1L;

        when(votingRepository.updateVotingStatus(any(Long.class), any(String.class))).thenReturn(1);

        int resultUpdateVotingStatus = votingControlService.openNewVoting(updatebleVoting);

        Assertions.assertEquals(updatebleVoting, resultUpdateVotingStatus);
        verify(votingRepository, times(1)).updateVotingStatus(any(Long.class), any(String.class));
    }

    @Test
    public void openNewVoting_error() {
        Long updatebleVoting = 1L;

        when(votingRepository.updateVotingStatus(any(Long.class), any(String.class))).thenReturn(2);

        Assertions.assertThrows(Exception.class, () -> votingControlService.openNewVoting(updatebleVoting));
    }
}
