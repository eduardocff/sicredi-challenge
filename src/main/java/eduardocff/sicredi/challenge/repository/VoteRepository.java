package eduardocff.sicredi.challenge.repository;

import eduardocff.sicredi.challenge.domain.Vote;
import eduardocff.sicredi.challenge.domain.VotePK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, VotePK> {

    @Query(value = "select * from Vote where VOTING_ID = :id", nativeQuery = true)
    List<Vote> findVotes(@Param(value = "id") final Long votingId);
}