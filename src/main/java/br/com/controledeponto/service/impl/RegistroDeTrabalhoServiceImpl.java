package br.com.controledeponto.service.impl;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.RegistroDeTrabalhoRepository;
import br.com.controledeponto.service.RegistroDeTrabalhoService;

import java.time.LocalDate;
import java.util.Optional;

public class RegistroDeTrabalhoServiceImpl implements RegistroDeTrabalhoService {

	private RegistroDeTrabalhoRepository registroDeTrabalhoRepository;

	RegistroDeTrabalhoServiceImpl(RegistroDeTrabalhoRepository registroDeTrabalhoRepository) {
		this.registroDeTrabalhoRepository = registroDeTrabalhoRepository;
	}

	@Override
	public Optional<RegistroDeTrabalho> obterRegistroDeTrabalhoPorData(LocalDate data) {
		return registroDeTrabalhoRepository.findByDia(data);
	}
}
