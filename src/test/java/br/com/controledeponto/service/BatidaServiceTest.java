package br.com.controledeponto.service;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.impl.BatidaServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ContextConfiguration(
	classes = BatidaServiceImpl.class
)
@ExtendWith(SpringExtension.class)
class BatidaServiceTest {

	@Autowired
	private BatidaService batidaService;

	@MockBean
	private MomentoRepository momentoRepository;

	@MockBean
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	@BeforeEach
	void setup() {
		Mockito.when(momentoRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));
	}

	@Test
	void deveSalvarMomentoEAdicionaloAoRegistroDeTrabalhoNoDia() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		Momento momento = new Momento(LocalDateTime.parse("2021-01-01T08:00:00"));

		Mockito.when(registroDeTrabalhoService.adicionarMomentoAoSeuRegistroDeTrabalho(momento))
				.thenReturn(new RegistroDeTrabalho(LocalDate.parse("2021-01-01")));

		Momento momentoSalvo = batidaService.salvarMomento(momento);

		Mockito.verify(registroDeTrabalhoService, Mockito.times(1)).adicionarMomentoAoSeuRegistroDeTrabalho(momento);
		Assertions.assertThat(momentoSalvo).isEqualTo(momento);
		Assertions.assertThat(momentoSalvo.getRegistroDeTrabalho()).isNotNull();
	}
}