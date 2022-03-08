package br.com.controledeponto.service;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.model.Momento;
import org.springframework.stereotype.Service;

@Service
public interface MomentoService {
	Momento salvarMomento(Momento momento) throws
		NaoPodeHaverMaisDeQuatroRegistrosException,
		NaoPodeRegistrarHorasEmFinalDeSemanaException,
		HorarioInferiorAoUltimoRegistradoException,
		DeveHaverNoMinimoUmaHoraDeAlmocoException,
		HorarioJaRegistradoException;
}
