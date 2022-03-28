package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.domain.Vote;
import eduardocff.sicredi.challenge.domain.VotePK;
import eduardocff.sicredi.challenge.enums.UserInfoStatus;
import eduardocff.sicredi.challenge.enums.VoteStatus;
import eduardocff.sicredi.challenge.exception.AssociateCanNotVoteException;
import eduardocff.sicredi.challenge.model.v1.UserInfoStatusDTO;
import eduardocff.sicredi.challenge.model.v1.VoteDTO;
import eduardocff.sicredi.challenge.model.v1.VotingResultDTO;
import eduardocff.sicredi.challenge.repository.VoteRepository;
import eduardocff.sicredi.challenge.utils.CastUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public VoteDTO saveNewVote(Long id, VoteDTO voteDTO)  {
        Vote vote = (Vote) CastUtil.copyFields(voteDTO, Vote.class);
        vote.setVotingId(id);
        vote.setVoteStatus(voteDTO.getVoteStatus());
        log.info(String.format("Associate %s voted %s in Voting %d", voteDTO.getAssociateId(), voteDTO.getVoteStatus(), id));
        return (VoteDTO) CastUtil.copyFields(voteRepository.save(vote), VoteDTO.class);
    }

    public boolean checkAlreadyVote(Long id, VoteDTO voteDTO) {
        VotePK votePK = new VotePK(id, voteDTO.getAssociateId());
        return voteRepository.findById(votePK).isPresent();
    }

    public void checkStatusAssociateCanVote(UserInfoStatusDTO userInfoStatusDTO) {
        if (userInfoStatusDTO == null ||  userInfoStatusDTO.getStatus().isEmpty() ||
                userInfoStatusDTO.getStatus().equals(UserInfoStatus.UNABLE_TO_VOTE.getStatus())) {
            log.error(String.format("Associate cannot vote because status is %s", userInfoStatusDTO.getStatus()));
            throw new AssociateCanNotVoteException("Associate cannot vote.");
        }
    }

    public VotingResultDTO countVotes(Long id) {
        List<Vote> votes = voteRepository.findVotes(id);

        VotingResultDTO resultDTO = new VotingResultDTO();
        resultDTO.setId(id);

        int countYesVotes = (int) votes.stream().filter(vote -> vote.getVoteStatus().equals(VoteStatus.Sim)).count();
        int countNoVotes = (int) votes.stream().filter(vote -> vote.getVoteStatus().equals(VoteStatus.Nao)).count();

        resultDTO.setVotesYes(countYesVotes);
        resultDTO.setVotesNo(countNoVotes);

        return resultDTO;
    }

}
