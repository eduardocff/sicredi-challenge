package eduardocff.sicredi.challenge.enums;

import lombok.Getter;

@Getter
public enum UserInfoStatus {
    ABLE_TO_VOTE("ABLE_TO_VOTE"),
    UNABLE_TO_VOTE("UNABLE_TO_VOTE");

    private String status;

    UserInfoStatus(String status) {
        this.status = status;
    }
}
