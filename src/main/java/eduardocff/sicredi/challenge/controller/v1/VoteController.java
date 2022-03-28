package eduardocff.sicredi.challenge.controller.v1;

import eduardocff.sicredi.challenge.exception.AssociateAlreadyVotedException;
import eduardocff.sicredi.challenge.exception.AssociateCanNotVoteException;
import eduardocff.sicredi.challenge.exception.EntityNotFoundException;
import eduardocff.sicredi.challenge.exception.VotingNotOpenException;
import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import eduardocff.sicredi.challenge.model.v1.UserInfoStatusDTO;
import eduardocff.sicredi.challenge.model.v1.VoteDTO;
import eduardocff.sicredi.challenge.model.v1.VotingDTO;
import eduardocff.sicredi.challenge.service.AssociateService;
import eduardocff.sicredi.challenge.service.VoteService;
import eduardocff.sicredi.challenge.service.VotingControlService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@Slf4j
@RequestMapping("api/v1/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VotingControlService votingControlService;

    @Autowired
    private AssociateService associateService;

    @PostMapping(path = "/{votingId}")
    @ApiOperation(value = "Register a new vote in the database",
            notes = "It is only possible to register votes in open voting.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created a new Vote successfully"),
            @ApiResponse(code = 412, message = "Voting not opened or associate cannot vote."),
    })
    public ResponseEntity createNewVote(@PathVariable("votingId") Long votingId, @RequestBody VoteDTO voteDTO) {

        try {
            VotingDTO votingDTO = votingControlService.findVotingById(votingId);

            votingControlService.checkVotingStatusToVote(votingDTO);

            AssociateDTO associate = associateService.findAssociateById(voteDTO.getAssociateId());

            UserInfoStatusDTO userInfoStatusDTO = associateService.checkAssociateCanVote(associate.getCpf());

            voteService.checkStatusAssociateCanVote(userInfoStatusDTO);

            boolean alreadyVote = voteService.checkAlreadyVote(voteDTO.getAssociateId(), voteDTO);

            if(alreadyVote) throw new AssociateAlreadyVotedException(String.format("Associate %s already voted.", voteDTO.getAssociateId()));

            VoteDTO votedDTO = voteService.saveNewVote(votingId, voteDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(votedDTO);

        } catch (EntityNotFoundException exception) {
            log.error("Voting/Associate not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (VotingNotOpenException | AssociateCanNotVoteException exception) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        } catch (HttpClientErrorException exception) {
            log.error(String.format("External API return 404 for Associate %s", voteDTO.getAssociateId()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
