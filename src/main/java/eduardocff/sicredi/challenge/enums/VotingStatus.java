package eduardocff.sicredi.challenge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VotingStatus {
    CREATED("CREATED"),
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String status;
}
