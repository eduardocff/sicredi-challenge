package eduardocff.sicredi.challenge.enums;

public enum VoteStatus {
    Sim("Sim"),
    Nao("Não");

    private String status;

    VoteStatus(String status) {
        this.status = status;
    }
}