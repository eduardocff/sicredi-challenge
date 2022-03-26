package eduardocff.sicredi.challenge.controller.v1;

import com.google.gson.Gson;
import eduardocff.sicredi.challenge.model.v1.AssociateDTO;
import eduardocff.sicredi.challenge.service.AssociateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AssociateControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AssociateController associateController;

    @Mock
    private AssociateService associateService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(associateController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView()).build();
    }

    @Test
    public void createNewAssociate_ok() throws Exception {
        var associateDTO = getAssociateDTO(null);
        var returnAssociateDTO = getAssociateDTO(1L);

        Gson gson = new Gson();

        when(associateService.addAssociate(associateDTO)).thenReturn(returnAssociateDTO);

        mockMvc.perform(
                post("/api/v1/associate")
                    .content(gson.toJson(associateDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void createNewAssociate_invalidCPF() throws Exception {
        var associateDTO = new AssociateDTO();
        associateDTO.setCpf(21L);

        when(associateService.addAssociate(associateDTO)).thenThrow(new IllegalArgumentException());

        Gson gson = new Gson();

        mockMvc.perform(
                post("/api/v1/associate")
                    .content(gson.toJson(associateDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createNewAssociate_AssociateNull() throws Exception {
        Gson gson = new Gson();

        mockMvc.perform(
                post("/api/v1/associate")
                        .content(gson.toJson(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private AssociateDTO getAssociateDTO(Long id) {
        var associateDTO = new AssociateDTO();
        associateDTO.setCpf(97198042031L);
        associateDTO.setName("Eduardo");

        if(id != null) {
            associateDTO.setId(id);
        }

        return associateDTO;
    }
}
