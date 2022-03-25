package eduardocff.sicredi.challenge.client;

import eduardocff.sicredi.challenge.model.v1.UserInfoStatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserInfoClient {

    @Value("${userinfo.checkCpf.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public UserInfoStatusDTO getAssociateSituation(Long cpf) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(url)
                .path(cpf.toString());

        return restTemplate.getForObject(builder.toUriString(), UserInfoStatusDTO.class);
    }
}
