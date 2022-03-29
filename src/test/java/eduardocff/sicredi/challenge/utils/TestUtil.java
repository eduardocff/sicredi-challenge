package eduardocff.sicredi.challenge.utils;

import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {
    public AssociateDTO getAssociateDTO(Long id) {
        var associateDTO = new AssociateDTO();
        associateDTO.setCpf(97198042031L);
        associateDTO.setName("Eduardo");

        return associateDTO;
    }
}
