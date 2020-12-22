package com.github.thestyleofme.datax.server.infra.autoconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/10/19 11:26
 * @since 1.0.0
 */
@Configuration("dataxAdminSwagger2Configuration")
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("datax-admin")
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Datax-Admin Restful APIs")
                .description("Datax-Admin")
                .termsOfServiceUrl("http://127.0.0.1")
                .contact(new Contact("thestyleofme", "", "thestyleofme@163.com"))
                .version("1.0.0")
                .build();
    }

}
