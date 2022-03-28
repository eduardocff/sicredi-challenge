package eduardocff.sicredi.challenge.repository;

import eduardocff.sicredi.challenge.domain.Vote;
import eduardocff.sicredi.challenge.domain.VotePK;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, VotePK> {
}