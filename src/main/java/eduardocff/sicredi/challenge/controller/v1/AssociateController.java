package eduardocff.sicredi.challenge.controller.v1;

import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import eduardocff.sicredi.challenge.service.AssociateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("api/v1/associate")
public class AssociateController {

    @Autowired
    private AssociateService associateService;

    @PostMapping
    public ResponseEntity addNewAssociate(@RequestBody AssociateDTO associateDTO) throws Exception {
        //TODO: add error returns
        return ResponseEntity.ok(associateService.addAssociate(associateDTO));
    }
}
