package br.com.controledeponto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SpringFoxConfig extends WebMvcConfigurationSupport {

	private String titulo = "Controle de Ponto API";
	private String descricao = "Uma API para gerenciar o controle de ponto dos funcionários de uma organização.";
	private String versao = "v1";
	private String nomeContato = "Richard Nascimento";
	private String emailContato = "rcdwoods@gmail.com";

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("br.com.controledeponto"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(titulo)
			.description(descricao)
			.version(versao)
			.contact(new Contact(nomeContato, null, emailContato))
			.build();
	}
}
