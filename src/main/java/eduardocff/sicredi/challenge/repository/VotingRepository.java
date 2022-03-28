package eduardocff.sicredi.challenge.repository;

import eduardocff.sicredi.challenge.domain.Voting;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VotingRepository extends CrudRepository<Voting, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Voting voting " +
           "SET voting.status = :status " +
           "WHERE voting.id = :id")
    Integer updateVotingStatus(@Param("id") Long id,
                           @Param("status") String status);
}
