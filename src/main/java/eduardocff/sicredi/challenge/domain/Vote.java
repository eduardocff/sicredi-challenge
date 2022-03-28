package eduardocff.sicredi.challenge.domain;

import eduardocff.sicredi.challenge.enums.VoteStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@IdClass(VotePK.class)
public class Vote {
    @Id
    private Long votingId;

    @Id
    private Long associateId;

    private VoteStatus voteStatus;

    @ManyToOne
    @JoinColumn(name = "votingId", referencedColumnName = "id", insertable = false, updatable = false)
    private Voting voting;

    @ManyToOne
    @JoinColumn(name = "associateId", referencedColumnName = "id", insertable = false, updatable = false)
    private Associate associate;
}
