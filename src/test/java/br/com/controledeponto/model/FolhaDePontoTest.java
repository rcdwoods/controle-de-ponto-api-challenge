package br.com.controledeponto.model;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

class FolhaDePontoTest {

	@Test
	void deveCalcularHorasTotaisTrabalhadasNoMes() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoCompleto("2021-01-04"),
			criarRegistroDeTrabalhoCompleto("2021-01-05"),
			criarRegistroDeTrabalhoCompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasTrabalhadas()).isEqualTo(Duration.parse("PT24H0M0S"));
	}

	@Test
	void deveGerarHorasExcedentesQuandoFuncionarioTrabalharAMais() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoExcedente("2021-01-04"),
			criarRegistroDeTrabalhoExcedente("2021-01-05"),
			criarRegistroDeTrabalhoExcedente("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasExcedentes()).isEqualTo(Duration.parse("PT1H30M0S"));
	}

	@Test
	void naoDeveGerarHorasExcedentesQuandoFuncionarioTrabalharHorarioNormal() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoCompleto("2021-01-04"),
			criarRegistroDeTrabalhoCompleto("2021-01-05"),
			criarRegistroDeTrabalhoCompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasExcedentes()).isEqualTo(Duration.parse("PT0S"));
	}

	@Test
	void naoDeveGerarHorasExcedentesQuandoFuncionarioTrabalharHorasAMenos() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoIncompleto("2021-01-04"),
			criarRegistroDeTrabalhoIncompleto("2021-01-05"),
			criarRegistroDeTrabalhoIncompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasExcedentes()).isEqualTo(Duration.parse("PT0S"));
	}

	@Test
	void deveGerarHorasDevidasQuandoFuncionarioTrabalharAMenos() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoIncompleto("2021-01-04"),
			criarRegistroDeTrabalhoIncompleto("2021-01-05"),
			criarRegistroDeTrabalhoIncompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasDevidas()).isEqualTo(Duration.parse("PT4H31M30S"));
	}

	@Test
	void naoDeveGerarHorasDevidasQuandoFuncionarioTrabalharHorarioCompleto() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoCompleto("2021-01-04"),
			criarRegistroDeTrabalhoCompleto("2021-01-05"),
			criarRegistroDeTrabalhoCompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasDevidas()).isEqualTo(Duration.parse("PT0S"));
	}

	@Test
	void naoDeveGerarHorasDevidasQuandoFuncionarioTrabalharAMais() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoExcedente("2021-01-04"),
			criarRegistroDeTrabalhoExcedente("2021-01-05"),
			criarRegistroDeTrabalhoExcedente("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasDevidas()).isEqualTo(Duration.parse("PT0S"));
	}

	private RegistroDeTrabalho criarRegistroDeTrabalhoCompleto(String data) throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T18:00:00")));
		return registroDeTrabalho;
	}

	private RegistroDeTrabalho criarRegistroDeTrabalhoExcedente(String data) throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T18:30:00")));
		return registroDeTrabalho;
	}

	private RegistroDeTrabalho criarRegistroDeTrabalhoIncompleto(String data) throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T16:29:30")));
		return registroDeTrabalho;
	}
}