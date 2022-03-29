package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.domain.Vote;
import eduardocff.sicredi.challenge.domain.VotePK;
import eduardocff.sicredi.challenge.enums.VoteStatus;
import eduardocff.sicredi.challenge.model.v1.VoteDTO;
import eduardocff.sicredi.challenge.model.v1.VoteResultDTO;
import eduardocff.sicredi.challenge.model.v1.VotingResultDTO;
import eduardocff.sicredi.challenge.repository.VoteRepository;
import eduardocff.sicredi.challenge.utils.CastUtil;
import eduardocff.sicredi.challenge.utils.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private VoteRepository voteRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void saveNewVote_ok() {
        Long id = 1L;

        var voteDTO = new VoteDTO();
        voteDTO.setAssociateId(1L);
        voteDTO.setVoteStatus(VoteStatus.Sim);

        var voteResultDTO = new VoteResultDTO();
        voteResultDTO.setAssociateId(voteDTO.getAssociateId());
        voteResultDTO.setVoteStatus(voteDTO.getVoteStatus());
        voteResultDTO.setVotingId(id);

        var vote = (Vote) CastUtil.copyFields(voteDTO, Vote.class);
        var returnVote = (Vote) CastUtil.copyFields(voteResultDTO, Vote.class);


        when(voteRepository.save(vote)).thenReturn(returnVote);

        voteService.saveNewVote(id, voteDTO);

        verify(voteRepository, times(1)).save(any(Vote.class));

    }

    @Test
    public void checkAlreadyVote() {
        var votePK = new VotePK(1L, 1L);

        var vote = new Vote();
        vote.setVoteStatus(VoteStatus.Sim);
        vote.setVotingId(1L);
        vote.setAssociateId(1L);

        var voteDTO = new VoteDTO();
        voteDTO.setAssociateId(1L);
        voteDTO.setVoteStatus(VoteStatus.Sim);

        when(voteRepository.findById(votePK)).thenReturn(Optional.of(vote));

        voteService.checkAlreadyVote(1L, voteDTO);

        verify(voteRepository, times(1)).findById(any(VotePK.class));
    }

    @Test
    public void countVotes() {
        var vote = new Vote();
        vote.setVoteStatus(VoteStatus.Sim);
        vote.setVotingId(1L);
        vote.setAssociateId(1L);

        List<Vote> votes = new ArrayList<>();
        votes.add(vote);

        var votingResultDTO = new VotingResultDTO();
        votingResultDTO.setId(1L);
        votingResultDTO.setVotesNo(0);
        votingResultDTO.setVotesYes(1);

        when(voteRepository.findVotes(1L)).thenReturn(votes);

        var returnVotingResult = voteService.countVotes(1L);

        Assertions.assertEquals(votingResultDTO.getId(), returnVotingResult.getId());
        Assertions.assertEquals(votingResultDTO.getVotesNo(), returnVotingResult.getVotesNo());
        Assertions.assertEquals(votingResultDTO.getVotesYes(), returnVotingResult.getVotesYes());
    }
}
