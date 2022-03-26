package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.domain.Associate;
import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import eduardocff.sicredi.challenge.repository.AssociateRepository;
import eduardocff.sicredi.challenge.utils.CastUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociateService {

    @Autowired
    private AssociateRepository associateRepository;

    private static String REGEX_CPF = "\\d{11}";

    public AssociateDTO addAssociate(AssociateDTO associateDTO) {
        if (associateDTO == null || associateDTO.getCpf() == null) {
            throw new IllegalArgumentException();
        }

        if (!checkValidCpf(associateDTO.getCpf())) {
            throw new IllegalArgumentException();
        }

        var associate = (Associate) CastUtil.copyFields(associateDTO, Associate.class);

        return (AssociateDTO) CastUtil.copyFields(associateRepository.save(associate), AssociateDTO.class);
    }

    private boolean checkValidCpf(Long cpf) {
        return cpf.toString().matches(REGEX_CPF);
    }
}
