package eduardocff.sicredi.challenge.model.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingResultDTO {
    private Long id;
    private int votesYes;
    private int votesNo;

    @Override
    public String toString() {
        return "Id: " + id +
                " SIM: " + votesYes +
                " NÃO: " + votesNo;
    }
}
