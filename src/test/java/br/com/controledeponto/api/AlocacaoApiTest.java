package br.com.controledeponto.api;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.AlocacaoRepository;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.RegistroDeTrabalhoService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.swagger.model.Alocacao;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AlocacaoApiTest {

	@MockBean
	private AlocacaoRepository alocacaoRepository;

	@MockBean
	private MomentoRepository momentoRepository;

	@MockBean
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	@LocalServerPort
	int randomServerPort;

	private String alocacoesApi;

	@BeforeEach
	void setup() {
		RestAssured.port = randomServerPort;
		this.alocacoesApi = "http://localhost:" + randomServerPort + "/controle-de-ponto/v1/alocacoes";
		Mockito.when(alocacaoRepository.save(Mockito.any())).thenAnswer(it -> it.getArgument(0));
	}

	@Test
	void deveAlocarHorasQuandoFuncionarioTiverHorasTrabalhadasSuficiente() {
		LocalDate diaDaAlocacao = LocalDate.parse("2021-01-01");

		Alocacao novaAlocacao = new Alocacao()
			.dia(diaDaAlocacao)
			.tempo("PT04H0S")
			.nomeProjeto("BMW");

		RegistroDeTrabalho registroDoDia = new RegistroDeTrabalho();
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T08:00:00")));
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T12:00:00")));

		Mockito.when(registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(diaDaAlocacao)).thenReturn(Optional.of(registroDoDia));

		given()
			.contentType(ContentType.JSON)
			.body(novaAlocacao)
			.when()
			.post(alocacoesApi)
			.then()
			.statusCode(HttpStatus.SC_CREATED)
			.assertThat()
			.body("tempo", is("PT4H"));
	}

	@Test
	void deveAlocarHorasQuandoFuncionarioTiverHorasTrabalhadasJaAlocadasMasAindaFaltarHorasParaAlocacao() {
		LocalDate diaDaAlocacao = LocalDate.parse("2021-01-01");

		Alocacao novaAlocacao = new Alocacao()
			.dia(diaDaAlocacao)
			.tempo("PT01H")
			.nomeProjeto("BMW");

		br.com.controledeponto.model.Alocacao alocacaoExistente = new br.com.controledeponto.model.Alocacao();
		alocacaoExistente.setTempo(Duration.parse("PT3H"));

		RegistroDeTrabalho registroDoDia = new RegistroDeTrabalho();
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T08:00:00")));
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T12:00:00")));

		Mockito.when(registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(diaDaAlocacao)).thenReturn(Optional.of(registroDoDia));
		Mockito.when(alocacaoRepository.findAllByDia(diaDaAlocacao)).thenReturn(List.of(alocacaoExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novaAlocacao)
			.when()
			.post(alocacoesApi)
			.then()
			.statusCode(HttpStatus.SC_CREATED)
			.assertThat()
			.body("tempo", is("PT1H"));
	}

	@Test
	void deveLancarExceptionQuandoColaboradorNaoTiverHorasTrabalhadasSuficiente() {
		LocalDate diaDaAlocacao = LocalDate.parse("2021-01-01");

		Alocacao novaAlocacao = new Alocacao()
			.dia(diaDaAlocacao)
			.tempo("PT04H1S")
			.nomeProjeto("BMW");

		RegistroDeTrabalho registroDoDia = new RegistroDeTrabalho();
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T08:00:00")));
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T12:00:00")));

		Mockito.when(registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(diaDaAlocacao)).thenReturn(Optional.of(registroDoDia));

		given()
			.contentType(ContentType.JSON)
			.body(novaAlocacao)
			.when()
			.post(alocacoesApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Não pode alocar tempo maior que o tempo trabalhado no dia"));
	}

	@Test
	void deveLancarExceptionQuandoColaboradorTiverTrabalhadoSuficienteMasJaTiverAlocadoEssasHorasEmOutroProjeto() {
		LocalDate diaDaAlocacao = LocalDate.parse("2021-01-01");

		Alocacao novaAlocacao = new Alocacao()
			.dia(diaDaAlocacao)
			.tempo("PT02H")
			.nomeProjeto("BMW");

		br.com.controledeponto.model.Alocacao alocacaoExistente = new br.com.controledeponto.model.Alocacao();
		alocacaoExistente.setTempo(Duration.parse("PT3H"));

		RegistroDeTrabalho registroDoDia = new RegistroDeTrabalho();
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T08:00:00")));
		registroDoDia.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T12:00:00")));

		Mockito.when(registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(diaDaAlocacao)).thenReturn(Optional.of(registroDoDia));
		Mockito.when(alocacaoRepository.findAllByDia(diaDaAlocacao)).thenReturn(List.of(alocacaoExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novaAlocacao)
			.when()
			.post(alocacoesApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Não possui tempo disponível para alocação"));
	}
}