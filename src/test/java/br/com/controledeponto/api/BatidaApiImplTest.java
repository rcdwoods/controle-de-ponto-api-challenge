package br.com.controledeponto.api;

import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.repository.RegistroDeTrabalhoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.swagger.model.Momento;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "local.server.port=8080")
@ActiveProfiles("test")
class BatidaApiImplTest {

	@MockBean
	private MomentoRepository momentoRepository;

	@MockBean
	private RegistroDeTrabalhoRepository registroDeTrabalhoRepository;

	@Captor
	private ArgumentCaptor<br.com.controledeponto.model.Momento> momentoCaptor;

	private String batidasApi = "http://localhost:8080/controle-de-ponto/v1/batidas";

	@BeforeEach
	void setup() {
		RestAssured.port = 8080;
		Mockito.when(momentoRepository.save(Mockito.any())).thenAnswer(it -> it.getArgument(0));
		Mockito.when(registroDeTrabalhoRepository.save(Mockito.any())).thenAnswer(it -> it.getArgument(0));
	}

	@Test
	void deveRegistrarUmMomentoECriarUmRegistroDeTrabalhoQuandoNaoExistirMomentosNoDia() {
		LocalDate diaDoRegistro = LocalDate.parse("2021-01-01");
		Momento novoMomento = new Momento();
		novoMomento.setDataHora("2021-01-01T08:00:00");

		Mockito.when(registroDeTrabalhoRepository.findByDia(diaDoRegistro)).thenReturn(Optional.empty());

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_CREATED)
			.assertThat()
			.body("dataHora", is(novoMomento.getDataHora()));

	}

	@Test
	void deveRegistrarUmMomentoMasNaoDeveCriarUmRegistroQuandoJaExistiremRegistrosNoDia() {
		LocalDate diaDoRegistro = LocalDate.parse("2021-01-01");
		RegistroDeTrabalho registroExistente = new RegistroDeTrabalho();
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(8, 0)));
		Momento novoMomento = new Momento();
		novoMomento.setDataHora("2021-01-01T12:00:00");

		Mockito.when(registroDeTrabalhoRepository.findByDia(diaDoRegistro)).thenReturn(Optional.of(registroExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_CREATED)
			.assertThat()
			.body("dataHora", is(novoMomento.getDataHora()));

		Mockito.verify(momentoRepository).save(momentoCaptor.capture());
		br.com.controledeponto.model.Momento momentoSaved = momentoCaptor.getValue();

		Assertions.assertThat(momentoSaved.getRegistroDeTrabalho().getMomentosRegistrados()).hasSize(2);
	}

	@Test
	void deveLancarUmaExceptionQuandoJaExistirMomentoRegistradoNoHorario() {
		LocalDate diaDoRegistro = LocalDate.parse("2021-01-01");
		RegistroDeTrabalho registroExistente = new RegistroDeTrabalho();
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(8, 0)));
		Momento novoMomento = new Momento();
		novoMomento.setDataHora("2021-01-01T08:00:00");

		Mockito.when(registroDeTrabalhoRepository.findByDia(diaDoRegistro)).thenReturn(Optional.of(registroExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_CONFLICT)
			.assertThat()
			.body("mensagem", is("Horários já registrado"));
	}

	@Test
	void deveLancarUmaExceptionQuandoJaExistiremQuatroHorariosRegistrados() {
		LocalDate diaDoRegistro = LocalDate.parse("2021-01-01");
		RegistroDeTrabalho registroExistente = new RegistroDeTrabalho();
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(8, 0)));
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(12, 0)));
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(13, 0)));
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(17, 0)));
		Momento novoMomento = new Momento();
		novoMomento.setDataHora("2021-01-01T18:00:00");

		Mockito.when(registroDeTrabalhoRepository.findByDia(diaDoRegistro)).thenReturn(Optional.of(registroExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_FORBIDDEN)
			.assertThat()
			.body("mensagem", is("Apenas 4 horários podem ser registrados por dia"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"2021-01-01T25:00:00", "2021-13-01T08:00:00", "20211301080000"})
	void deveLancarUmaExceptionQuandoDataHoraPossuirUmFormatoInvalido(String dataInvalida) {
		Momento novoMomento = new Momento();
		novoMomento.setDataHora(dataInvalida);

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Data e hora em formato inválido"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"2021-01-01T12:00:01", "2021-01-01T12:30:00", "2021-01-01T12:59:59"})
	void deveLancarUmaExceptionQuandoHouverMenosDoQueUmaHoraDeAlmoco(String dataHoraComMenosDeUmaHoraDeAlmoco) {
		LocalDate diaDoRegistro = LocalDate.parse("2021-01-01");
		RegistroDeTrabalho registroExistente = new RegistroDeTrabalho();
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(8, 0)));
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(12, 0)));
		Momento novoMomento = new Momento();
		novoMomento.setDataHora(dataHoraComMenosDeUmaHoraDeAlmoco);

		Mockito.when(registroDeTrabalhoRepository.findByDia(diaDoRegistro)).thenReturn(Optional.of(registroExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Deve haver no mínimo 1 hora de almoço"));
	}

	@Test
	void deveLancarUmaExceptionQuandoHorarioForInferiorAoUltimoRegistrado() {
		LocalDate diaDoRegistro = LocalDate.parse("2021-01-01");
		RegistroDeTrabalho registroExistente = new RegistroDeTrabalho();
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(8, 0)));
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(12, 0)));
		registroExistente.registrarMomento(new br.com.controledeponto.model.Momento(diaDoRegistro.atTime(14, 0)));
		Momento novoMomento = new Momento();
		novoMomento.setDataHora("2021-01-01T13:00:00");

		Mockito.when(registroDeTrabalhoRepository.findByDia(diaDoRegistro)).thenReturn(Optional.of(registroExistente));

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Horário inferior ao último registrado"));
	}

	@Test
	void deveLancarUmaExceptionQuandoHorarioNaoForInformado() {
		Momento novoMomento = new Momento();

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Campo obrigatório não informado"));
	}

	@Test
	void deveLancarUmaExceptionQuandoHorarioForEmUmFinalDeSemana() {
		Momento novoMomento = new Momento();
		novoMomento.setDataHora("2022-03-12T18:00:00");

		given()
			.contentType(ContentType.JSON)
			.body(novoMomento)
			.when()
			.post(batidasApi)
			.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
			.assertThat()
			.body("mensagem", is("Sábado e domingo não são permitidos como dia de trabalho"));
	}
}
