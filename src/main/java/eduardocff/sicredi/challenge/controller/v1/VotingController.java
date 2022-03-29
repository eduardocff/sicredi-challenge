package eduardocff.sicredi.challenge.controller.v1;

import eduardocff.sicredi.challenge.enums.VotingStatus;
import eduardocff.sicredi.challenge.exception.EntityNotFoundException;
import eduardocff.sicredi.challenge.exception.VotingAlreadyOpenedException;
import eduardocff.sicredi.challenge.model.v1.VotingDTO;
import eduardocff.sicredi.challenge.model.v1.VotingInputDTO;
import eduardocff.sicredi.challenge.service.VotingControlService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("api/v1/voting")
public class VotingController {

    @Autowired
    private VotingControlService votingService;

    @PostMapping
    @ApiOperation(value = "Creates a new voting in the database",
            notes = "It is necessary to inform a valid Reason for the Voting.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created a new Voting successfully"),
            @ApiResponse(code = 400, message = "Problem with the Reason entered"),
    })
    public ResponseEntity createNewVoting(@RequestBody VotingInputDTO votingInputDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(votingService.createVoting(votingInputDTO));
        } catch (IllegalArgumentException exception) {
            log.error("Voting reason entered is empty or in an improper format.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping(path = "/start/{id}")
    @ApiOperation(value = "Start a voting",
            notes = "This voting must exist and its status must be CREATED.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Started a new Voting successfully"),
            @ApiResponse(code = 400, message = "Voting already opened"),
            @ApiResponse(code = 404, message = "Voting not found by id provided"),
    })
    public ResponseEntity startVoting(@PathVariable Long id) {
        try {
            VotingDTO voting = votingService.findVotingById(id);

            votingService.checkVotingStatusToOpen(voting);

            votingService.openNewVoting(id);

            voting.setStatus(VotingStatus.OPEN.getStatus());

            return ResponseEntity.ok(voting);

        } catch (EntityNotFoundException exception) {
            log.error("Voting not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (VotingAlreadyOpenedException exception) {
            log.error("Voting already opened or finished.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
