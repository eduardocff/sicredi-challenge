package eduardocff.sicredi.challenge.model.v1;

import eduardocff.sicredi.challenge.enums.VoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {

    private Long votingId;

    private Long associateId;

    private VoteStatus voteStatus;
}
