package eduardocff.sicredi.challenge.controller.v1;

import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import eduardocff.sicredi.challenge.service.AssociateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
    @ApiOperation(value = "Creates a new associate in the database",
            notes = "It is necessary to inform a valid CPF that is not present in the database (unique).")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created a new Associate successfully"),
            @ApiResponse(code = 400, message = "Problem with the CPF entered"),
            @ApiResponse(code = 409, message = "Conflict when trying to create an associate with an existing CPF in the database"),
            @ApiResponse(code = 500, message = "Internal server error to catch any possible database error.")
    })
    public ResponseEntity addNewAssociate(@RequestBody AssociateDTO associateDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(associateService.addAssociate(associateDTO));
        } catch (IllegalArgumentException exception) {
            log.error("cpf entered is empty or in an improper format.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DataIntegrityViolationException exception) {
            log.error("The associate already exists in the database for the entered cpf.");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception exception) {
            log.error("Something went wrong: ", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
