package br.com.controledeponto.service;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public interface RegistroDeTrabalhoService {
	Optional<RegistroDeTrabalho> obterRegistroDeTrabalhoPorData(LocalDate data);
	RegistroDeTrabalho adicionarMomentoAoSeuRegistroDeTrabalho(Momento momento) throws
		NaoPodeHaverMaisDeQuatroRegistrosException,
		NaoPodeRegistrarHorasEmFinalDeSemanaException,
		HorarioInferiorAoUltimoRegistradoException,
		DeveHaverNoMinimoUmaHoraDeAlmocoException,
		HorarioJaRegistradoException;
}
