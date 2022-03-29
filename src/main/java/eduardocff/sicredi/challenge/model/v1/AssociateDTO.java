package eduardocff.sicredi.challenge.model.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociateDTO {
    private Long cpf;
    private String name;
}
