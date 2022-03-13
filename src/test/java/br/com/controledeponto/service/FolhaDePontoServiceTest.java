package br.com.controledeponto.service;

import br.com.controledeponto.exception.NaoHaRegistrosDeTrabalhoNoMesException;
import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.FolhaDePonto;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.service.impl.FolhaDePontoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(
	classes = FolhaDePontoServiceImpl.class
)
@ExtendWith(SpringExtension.class)
class FolhaDePontoServiceTest {

	@Autowired
	private FolhaDePontoService folhaDePontoService;
	@MockBean
	private AlocacaoService alocacaoService;
	@MockBean
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	@Test
	void gerarFolhaDePontoComRegistrosDeTrabalhoESemAlocacoes() {
		YearMonth mes = YearMonth.parse("2021-01");
		List<RegistroDeTrabalho> registrosDoMes = new ArrayList<>(
			List.of(
				criarRegistroDeTrabalhoCompleto("2021-01-01"),
				criarRegistroDeTrabalhoCompleto("2021-01-05"),
				criarRegistroDeTrabalhoCompleto("2021-01-06")
			)
		);
		Mockito.when(registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(mes)).thenReturn(registrosDoMes);
		Mockito.when(alocacaoService.obterAlocacoesPorMes(mes)).thenReturn(new ArrayList<>());

		FolhaDePonto folhaDePontoDoMes = folhaDePontoService.gerarFolhaDePonto(mes);
		Assertions.assertThat(folhaDePontoDoMes.getMes()).isEqualTo(mes);
		Assertions.assertThat(folhaDePontoDoMes.getRegistrosDeTrabalho()).hasSize(3);
	}

	@Test
	void gerarFolhaDePontoComRegistrosDeTrabalhoEComAlocacoes() {
		YearMonth mes = YearMonth.parse("2021-01");
		List<RegistroDeTrabalho> registrosDoMes = new ArrayList<>(
			List.of(
				criarRegistroDeTrabalhoCompleto("2021-01-01"),
				criarRegistroDeTrabalhoCompleto("2021-01-05"),
				criarRegistroDeTrabalhoCompleto("2021-01-06")
			)
		);
		List<Alocacao> alocacoesDoMes = new ArrayList<>(
			List.of(
				new Alocacao(LocalDate.parse("2021-01-01"), Duration.ofHours(8), ""),
				new Alocacao(LocalDate.parse("2021-01-05"), Duration.ofHours(8), "")
			)
		);
		Mockito.when(registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(mes)).thenReturn(registrosDoMes);
		Mockito.when(alocacaoService.obterAlocacoesPorMes(mes)).thenReturn(alocacoesDoMes);

		FolhaDePonto folhaDePontoDoMes = folhaDePontoService.gerarFolhaDePonto(mes);
		Assertions.assertThat(folhaDePontoDoMes.getMes()).isEqualTo(mes);
		Assertions.assertThat(folhaDePontoDoMes.getRegistrosDeTrabalho()).hasSize(3);
		Assertions.assertThat(folhaDePontoDoMes.getAlocacoes()).hasSize(2);
	}

	@Test
	void deveLancarExceptionQuandoNaoExistiremRegistrosDeTrabalhoNoMes() {
		YearMonth mes = YearMonth.parse("2021-01");
		Mockito.when(registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(mes)).thenReturn(new ArrayList<>());

		Exception exception = assertThrows(NaoHaRegistrosDeTrabalhoNoMesException.class, () -> {
			folhaDePontoService.gerarFolhaDePonto(mes);
		});
		Assertions.assertThat(exception.getMessage()).isEqualTo("Não há registros de trabalho nesse mês para gerar um relatório.");
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