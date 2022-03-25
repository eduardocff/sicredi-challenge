package eduardocff.sicredi.challenge.repository;

import eduardocff.sicredi.challenge.domain.Associate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends CrudRepository<Associate, Long> {
}
