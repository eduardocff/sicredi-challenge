package eduardocff.sicredi.challenge.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@EnableSwagger2
@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("eduardocff.sicredi.challenge.controller.v1"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){

        Contact contact = new Contact("Eduardo Cardoso",
                StringUtils.EMPTY,
                "eduardof.cardoso@outlook.com");

        return new ApiInfo(
                "Sicredi Challenge",
                "Eduardo Cardoso",
                "1.0",
                "",
                contact,
                "LinkedIn",
                "https://www.linkedin.com/in/eduardo-cardoso-ferreira/",
                new ArrayList<>());
    }
}