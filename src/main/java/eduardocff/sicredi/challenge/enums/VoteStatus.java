package eduardocff.sicredi.challenge.enums;

public enum VoteStatus {
    YES("Sim"),
    NO("Não");

    private String status;

    VoteStatus(String status) {
        this.status = status;
    }
}