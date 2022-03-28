package eduardocff.sicredi.challenge.exception;

public class VotingAlreadyOpenedException extends RuntimeException {

    public VotingAlreadyOpenedException(String message) {
        super(message);
    }

}