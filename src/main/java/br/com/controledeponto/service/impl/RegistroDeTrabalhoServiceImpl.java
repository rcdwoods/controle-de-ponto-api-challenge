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

	@Override
	public RegistroDeTrabalho adicionarMomentoAoSeuRegistroDeTrabalho(Momento momento) {
		Optional<RegistroDeTrabalho> registroDeTrabalhoNoDia = obterRegistroDeTrabalhoPorData(momento.getDataHora().toLocalDate());

		if (registroDeTrabalhoNoDia.isEmpty())
			registroDeTrabalhoNoDia = Optional.of(criarRegistroDeTrabalho(new RegistroDeTrabalho(momento.getDataHora().toLocalDate())));

		registroDeTrabalhoNoDia.get().registrarMomento(momento);
		return registroDeTrabalhoRepository.save(registroDeTrabalhoNoDia.get());
	}

	private RegistroDeTrabalho criarRegistroDeTrabalho(RegistroDeTrabalho registroDeTrabalho) {
		return this.registroDeTrabalhoRepository.save(registroDeTrabalho);
	}
}
