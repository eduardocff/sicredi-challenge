package eduardocff.sicredi.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotePK implements Serializable {
    private Long associateId;
    private Long votingId;
}
