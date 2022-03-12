package br.com.controledeponto.api;

import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.AlocacaoService;
import br.com.controledeponto.service.RegistroDeTrabalhoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.swagger.model.Relatorio;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
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
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FolhaDePontoApiTest {

	@MockBean
	private AlocacaoService alocacaoService;

	@MockBean
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	@MockBean
	private MomentoRepository momentoRepository;

	@LocalServerPort
	int randomServerPort;

	private String folhasDePontoApi;

	@BeforeEach
	void setup() {
		RestAssured.port = randomServerPort;
		this.folhasDePontoApi = "http://localhost:" + randomServerPort + "/controle-de-ponto/v1/folhas-de-ponto";
	}

	@Test
	void deveGerarUmaFolhaDePontoQuandoExistiremRegistrosNoMes() {
		List<RegistroDeTrabalho> registrosDoMes = List.of(
			criarRegistroDeTrabalhoCompleto("2021-01-01"),
			criarRegistroDeTrabalhoCompleto("2021-01-05"),
			criarRegistroDeTrabalhoCompleto("2021-01-06")
		);
		List<Alocacao> alocacoesDoMes = List.of(
			new Alocacao(LocalDate.parse("2021-01-01"), Duration.ofHours(6), "BMW"),
			new Alocacao(LocalDate.parse("2021-01-05"), Duration.ofHours(6), "Livelo")
		);
		Mockito.when(registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(YearMonth.parse("2021-01"))).thenReturn(registrosDoMes);
		Mockito.when(alocacaoService.obterAlocacoesPorMes(YearMonth.parse("2021-01"))).thenReturn(alocacoesDoMes);

		given()
			.contentType(ContentType.JSON)
			.when()
			.post(folhasDePontoApi + "/2021-01")
			.then()
			.statusCode(HttpStatus.SC_CREATED)
			.assertThat()
			.body("horasTrabalhadas", is("PT24H"))
			.body("horasDevidas", is("PT0S"))
			.body("horasExcedentes", is("PT0S"))
			.body("registros", hasSize(3))
			.body("alocacoes", hasSize(2));
	}

	@Test
	void deveGerarUmaFolhaDePontoEOsRegistrosDevemPossuirDiaETempo() throws JsonProcessingException {
		List<RegistroDeTrabalho> registroDoMes = List.of(criarRegistroDeTrabalhoCompleto("2021-01-01"));
		Mockito.when(registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(YearMonth.parse("2021-01"))).thenReturn(registroDoMes);

		Relatorio relatorio = given()
			.contentType(ContentType.JSON)
			.when()
			.post(folhasDePontoApi + "/2021-01")
			.then()
			.statusCode(HttpStatus.SC_CREATED)
			.extract()
			.as(Relatorio.class);

		Assertions.assertThat(relatorio.getRegistros().get(0).getDia()).isEqualTo("2021-01-01");
		Assertions.assertThat(relatorio.getRegistros().get(0).getTempo()).isEqualTo("PT8H");
	}

	@Test
	void deveLancarExceptionQuandoNaoHouveremRegistrosNoMes() {
		Mockito.when(registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(YearMonth.parse("2021-01"))).thenReturn(List.of());

		given()
			.contentType(ContentType.JSON)
			.when()
			.post(folhasDePontoApi + "/2021-01")
			.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
			.assertThat()
			.body("mensagem", is("Não há registros de trabalho nesse mês para gerar um relatório."));
	}

	private RegistroDeTrabalho criarRegistroDeTrabalhoCompleto(String data) {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T17:00:00")));
		return registroDeTrabalho;
	}
}