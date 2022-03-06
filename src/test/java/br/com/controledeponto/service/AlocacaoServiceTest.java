package br.com.controledeponto.service;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.exception.NaoPossuiTempoDisponivelParaAlocacaoException;
import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.AlocacaoRepository;
import br.com.controledeponto.service.impl.AlocacaoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

@ContextConfiguration(classes = AlocacaoServiceImpl.class)
@ExtendWith(SpringExtension.class)
class AlocacaoServiceTest {

	@Autowired
	private AlocacaoService alocacaoService;

	@MockBean
	private AlocacaoRepository alocacaoRepository;

	@MockBean
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	@BeforeEach
	void setup() {
		Mockito.when(alocacaoRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));
	}

	@Test
	void deveAlocarHorasQuandoUsuarioTiverTrabalhadoOSuficiente() throws NaoPossuiTempoDisponivelParaAlocacaoException, NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException, NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		Alocacao alocacao = new Alocacao(LocalDate.parse("2021-01-01"), Duration.ofHours(2), "BMW");
		RegistroDeTrabalho registroDeTrabalhoDoDia = criarRegistroDeTrabalhoCompleto();

		Mockito.when(registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(LocalDate.parse("2021-01-01"))).thenReturn(Optional.of(registroDeTrabalhoDoDia));

		Alocacao alocacaoRealizada = alocacaoService.alocarHoras(alocacao);
		Assertions.assertThat(alocacaoRealizada).isEqualTo(alocacao);
	}

	@Test
	void naoDeveAlocarHorasQuandoUsuarioNaoTiverTrabalhadoOSuficiente() throws NaoPossuiTempoDisponivelParaAlocacaoException, NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException, NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		Alocacao alocacao = new Alocacao(LocalDate.parse("2021-01-01"), Duration.ofHours(9), "BMW");
		RegistroDeTrabalho registroDeTrabalhoDoDia = criarRegistroDeTrabalhoCompleto();

		Mockito.when(registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(LocalDate.parse("2021-01-01"))).thenReturn(Optional.of(registroDeTrabalhoDoDia));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException.class, () -> {
			alocacaoService.alocarHoras(alocacao);
		});
		Assertions.assertThat(exception.getMessage()).isEqualTo("Não é possível alocar um tempo maior que o tempo trabalhado no dia");
	}

	private RegistroDeTrabalho criarRegistroDeTrabalhoCompleto() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2021-01-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2021-01-01T17:00:00")));
		return registroDeTrabalho;
	}
}