package eduardocff.sicredi.challenge.service;

import eduardocff.sicredi.challenge.domain.Associate;
import eduardocff.sicredi.challenge.repository.AssociateRepository;
import eduardocff.sicredi.challenge.utils.CastUtil;
import eduardocff.sicredi.challenge.utils.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
public class AssociateServiceTest {

    @InjectMocks
    private AssociateService associateService;

    @Mock
    private AssociateRepository associateRepository;

    @Autowired
    private TestUtil testUtil;

    @Test
    public void addAssociate_ok() {
        var associateDTO = testUtil.getAssociateDTO(null);
        var returnAssociateDTO = testUtil.getAssociateDTO(1L);

        var associate = (Associate) CastUtil.copyFields(associateDTO, Associate.class);
        var returnAssociate = (Associate) CastUtil.copyFields(returnAssociateDTO, Associate.class);

        when(associateRepository.save(associate)).thenReturn(returnAssociate);

        var newAssociateDTO = associateService.addAssociate(associateDTO);

        verify(associateRepository, times(1)).save(any(Associate.class));
        Assertions.assertEquals(associateDTO.getCpf(), newAssociateDTO.getCpf());
        Assertions.assertEquals(associateDTO.getName(), newAssociateDTO.getName());
    }

    @Test
    public void addAssociate_doesNotThrow() {
        var associateDTO = testUtil.getAssociateDTO(null);
        var returnAssociateDTO = testUtil.getAssociateDTO(1L);

        var associate = (Associate) CastUtil.copyFields(associateDTO, Associate.class);
        var returnAssociate = (Associate) CastUtil.copyFields(returnAssociateDTO, Associate.class);

        when(associateRepository.save(associate)).thenReturn(returnAssociate);

        Assertions.assertDoesNotThrow(() -> associateService.addAssociate(associateDTO));
    }

    @Test
    public void addAssociate_cpfNull() {
        var associateDTO = testUtil.getAssociateDTO(null);

        associateDTO.setCpf(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> associateService.addAssociate(associateDTO));
    }

    @Test
    public void addAssociate_associateDTONull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> associateService.addAssociate(null));
    }

    @Test
    public void addAssociate_cpfInvalidFormat() {
        var associateDTO = testUtil.getAssociateDTO(null);

        associateDTO.setCpf(12345L);

        Assertions.assertThrows(IllegalArgumentException.class, () -> associateService.addAssociate(associateDTO));
    }


}
