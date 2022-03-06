package br.com.controledeponto.service;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.RegistroDeTrabalhoRepository;
import br.com.controledeponto.service.impl.RegistroDeTrabalhoServiceImpl;
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
import java.util.Optional;

@ContextConfiguration(
	classes = RegistroDeTrabalhoServiceImpl.class
)
@ExtendWith(SpringExtension.class)
class RegistroDeTrabalhoServiceTest {

	@Autowired
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	@MockBean
	private RegistroDeTrabalhoRepository registroDeTrabalhoRepository;

	@BeforeEach
	void setup() {
		Mockito.when(registroDeTrabalhoRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));
	}

	@Test
	void deveObterRegistrosDeTrabalhoNaData() {
		LocalDate dataDoRegistro = LocalDate.parse("2021-01-01");
		Mockito.when(registroDeTrabalhoRepository.findByDia(dataDoRegistro)).thenReturn(Optional.of(new RegistroDeTrabalho(dataDoRegistro)));
		Optional<RegistroDeTrabalho> registroDeTrabalhoEncontrado = registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(dataDoRegistro);
		Assertions.assertThat(registroDeTrabalhoEncontrado).isPresent();
	}
}