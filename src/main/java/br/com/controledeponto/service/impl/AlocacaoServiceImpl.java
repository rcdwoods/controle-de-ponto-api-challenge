package br.com.controledeponto.service.impl;

import br.com.controledeponto.exception.NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException;
import br.com.controledeponto.exception.NaoPossuiTempoDisponivelParaAlocacaoException;
import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.AlocacaoRepository;
import br.com.controledeponto.service.AlocacaoService;
import br.com.controledeponto.service.RegistroDeTrabalhoService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AlocacaoServiceImpl implements AlocacaoService {

	private AlocacaoRepository alocacaoRepository;
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	AlocacaoServiceImpl(AlocacaoRepository alocacaoRepository, RegistroDeTrabalhoService registroDeTrabalhoService) {
		this.alocacaoRepository = alocacaoRepository;
		this.registroDeTrabalhoService = registroDeTrabalhoService;
	}

	@Override
	public Alocacao alocarHoras(Alocacao alocacao) throws NaoPossuiTempoDisponivelParaAlocacaoException, NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException {
		return alocacaoRepository.save(alocacao);
	}
}
