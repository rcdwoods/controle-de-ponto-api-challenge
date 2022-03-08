package br.com.controledeponto.model;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

class RegistroDeTrabalhoTest {

	@Test
	void deveRegistrarMomentoQuandoNaoExistiremMomentosRegistrados() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		Momento momentoASerRegistrado = new Momento(LocalDateTime.parse("2022-12-01T08:00:00"));
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(momentoASerRegistrado);
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados()).contains(momentoASerRegistrado);
	}

	@Test
	void deveRegistrarQuatroMomentosQuandoTodosMomentosForemValidos() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados()).hasSize(4);
	}

	@Test
	void deveRegistrarMomentoQuandoJaExistirUmMomentoMasADataHoraForInterior() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados()).hasSize(2);
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados().get(0).getDataHora()).isEqualTo("2022-12-01T08:00:00");
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados().get(1).getDataHora()).isEqualTo("2022-12-01T09:00:00");
	}

	@Test
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoDataHoraForMenorDoQueOMomentoAnterior() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(HorarioInferiorAoUltimoRegistradoException.class, () -> {
			Momento momentoComHorarioMenor = new Momento(LocalDateTime.parse("2022-12-01T07:00:00"));
			registroDeTrabalho.registrarMomento(momentoComHorarioMenor);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Horário inferior ao último registrado.");
	}

	@Test
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoDataHoraJaTiverSidoRegistrada() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(HorarioJaRegistradoException.class, () -> {
			Momento momentoComHorarioJaRegistrado = new Momento(LocalDateTime.parse("2022-12-01T08:00:00"));
			registroDeTrabalho.registrarMomento(momentoComHorarioJaRegistrado);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Horários já registrado");
	}

	@Test
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoJaHouverQuatroMomentosRegistrados() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NaoPodeHaverMaisDeQuatroRegistrosException.class, () -> {
			Momento momentoComHorarioJaRegistrado = new Momento(LocalDateTime.parse("2022-12-01T19:00:00"));
			registroDeTrabalho.registrarMomento(momentoComHorarioJaRegistrado);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Apenas 4 horários podem ser registrados por dia");
	}

	@ParameterizedTest
	@ValueSource(strings = {"2022-12-01T12:01:00", "2022-12-01T12:59:00"})
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoHorarioDeAlmocoForMenorQueOMinimo(String dataHora) throws NaoPodeHaverMaisDeQuatroRegistrosException, HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(DeveHaverNoMinimoUmaHoraDeAlmocoException.class, () -> {
			Momento momentoComHorarioJaRegistrado = new Momento(LocalDateTime.parse(dataHora));
			registroDeTrabalho.registrarMomento(momentoComHorarioJaRegistrado);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Deve haver no mínimo 1 hora de almoço");
	}

	@ParameterizedTest
	@ValueSource(strings = {"2022-01-01T09:00:00", "2022-01-02T09:00:00"})
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoForEmUmFinalDeSemana(String dataHora) {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(dataHora.substring(0,10)));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NaoPodeRegistrarHorasEmFinalDeSemanaException.class, () -> {
			Momento momentoEmFinalDeSemana = new Momento(LocalDateTime.parse(dataHora));
			registroDeTrabalho.registrarMomento(momentoEmFinalDeSemana);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Sábado e domingo não são permitidos como dia de trabalho");
	}

	@Test
	void deveCalcularAsHorasTrabalhadasNoDia() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasTrabalhadas().toHours()).isEqualTo(8);
	}

	@Test
	void naoDeveGerarHorasExcedentesQuandoFuncionarioTrabalharOitoHoras() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasExcedentes().toHours()).isZero();
	}

	@Test
	void naoDeveGerarHorasExcedentesQuandoFuncionarioTrabalharMenosDoQueOitoHoras() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T17:59:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasExcedentes().toHours()).isZero();
	}

	@Test
	void deveGerarHorasExcedentesQuandoFuncionarioTrabalharMaisDoQueOitoHoras() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:01:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasExcedentes().toMinutes()).isEqualTo(1);
	}

	@Test
	void naoDeveGerarHorasDevidasQuandoFuncionarioTrabalharOitoHoras() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasDevidas().toHours()).isZero();
	}

	@Test
	void naoDeveGerarHorasDevidasQuandoFuncionarioTrabalharMaisDoQueOitoHoras() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:01:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasDevidas().toHours()).isZero();
	}

	@Test
	void deveGerarHorasDevidasQuandoFuncionarioTrabalharMenosDoQueOitoHoras() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T16:59:00")));

		Assertions.assertThat(registroDeTrabalho.getHorasDevidas().toMinutes()).isEqualTo(61);
	}
}
