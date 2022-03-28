package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.domain.Voting;
import eduardocff.sicredi.challenge.enums.VotingStatus;
import eduardocff.sicredi.challenge.exception.EntityNotFoundException;
import eduardocff.sicredi.challenge.exception.VotingAlreadyOpenedException;
import eduardocff.sicredi.challenge.exception.VotingNotOpenException;
import eduardocff.sicredi.challenge.rabbitMQ.Producer;
import eduardocff.sicredi.challenge.model.v1.VotingDTO;
import eduardocff.sicredi.challenge.repository.VotingRepository;
import eduardocff.sicredi.challenge.utils.CastUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VotingControlService {

    @Autowired
    private VotingRepository votingRepository;

    @Autowired
    private Producer messageProducer;

    @Autowired
    private VoteService voteService;

    public VotingDTO createVoting(VotingDTO votingDTO) {
        checkVotingReason(votingDTO);

        Voting voting = (Voting) CastUtil.copyFields(votingDTO, Voting.class);
        voting.setStatus(VotingStatus.CREATED.getStatus());

        Voting createdVoting = votingRepository.save(voting);

        return (VotingDTO) CastUtil.copyFields(createdVoting, VotingDTO.class);
    }

    private void checkVotingReason(VotingDTO votingDTO) {
        if (votingDTO == null ||
            votingDTO.getReason() == null ||
            votingDTO.getReason().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public int openNewVoting(Long id) throws Exception {
        //JPA return an Integer with the number of updated entities
        int updatableVotings = votingRepository.updateVotingStatus(id, VotingStatus.OPEN.getStatus());
        if (updatableVotings != 1) { throw new Exception(); }
        scheduleTheEndOfVoting(id);
        return updatableVotings;
    }

    private void scheduleTheEndOfVoting(Long id) {
        log.info(String.format("Voting completion %s schedule started.", id));

        //https://www.geeksforgeeks.org/scheduledexecutorservice-interface-in-java/
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable taskToCloseVoting = createTaskToCloseVoting(id);

        //Default voting time: 1 minute
        executorService.schedule(taskToCloseVoting, 1, TimeUnit.MINUTES);
        executorService.shutdown();
    }

    private Runnable createTaskToCloseVoting(Long id) {
        Runnable closeTask = () -> {
            log.info(String.format("The voting [%d] is being finalized.", id));
            votingRepository.updateVotingStatus(id, VotingStatus.CLOSED.getStatus());
            messageProducer.send(voteService.countVotes(id).toString());
        };
        return closeTask;
    }


    public VotingDTO findVotingById(Long id) {
        Voting voting = votingRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Voting not found by id: " + id);
        });

        return (VotingDTO) CastUtil.copyFields(voting, VotingDTO.class);
    }

    public void checkVotingStatusToOpen(VotingDTO votingDTO) {
        //checks that there is not already an open or closed voting with that ID.
        if (!votingDTO.getStatus().equals(VotingStatus.CREATED.getStatus())) {
            log.error(String.format("Voting cannot be opened because it is %s", votingDTO.getStatus()));
            throw new VotingAlreadyOpenedException("Voting cannot be opened as status is not created.");
        }
    }

    public void checkVotingStatusToVote(VotingDTO votingDTO) {
        //checks that there is not already an open or closed voting with that ID.
        if (!votingDTO.getStatus().equals(VotingStatus.OPEN.getStatus())) {
            log.error(String.format("It is not possible to vote in this voting, because it is %s", votingDTO.getStatus()));
            throw new VotingNotOpenException("Voting cannot be voted as status is not OPEN.");
        }
    }
}
