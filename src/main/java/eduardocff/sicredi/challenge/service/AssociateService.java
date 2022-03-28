package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.client.UserInfoClient;
import eduardocff.sicredi.challenge.domain.Associate;
import eduardocff.sicredi.challenge.exception.EntityNotFoundException;
import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import eduardocff.sicredi.challenge.model.v1.UserInfoStatusDTO;
import eduardocff.sicredi.challenge.repository.AssociateRepository;
import eduardocff.sicredi.challenge.utils.CastUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssociateService {

    @Autowired
    private AssociateRepository associateRepository;

    @Autowired
    private UserInfoClient userInfoClient;

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

    public AssociateDTO findAssociateById(Long id) {
        Associate associate = associateRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Associate not found by id: " + id);
        });

        return (AssociateDTO) CastUtil.copyFields(associate, AssociateDTO.class);
    }

    public UserInfoStatusDTO checkAssociateCanVote(Long cpf) {
        UserInfoStatusDTO userInfoStatusDTO = userInfoClient.getAssociateSituation(cpf);
        log.info(String.format("External API to check Cpf result: %s", userInfoStatusDTO.getStatus()));
        return userInfoStatusDTO;
    }

    private boolean checkValidCpf(Long cpf) {
        return cpf.toString().matches(REGEX_CPF);
    }
}
