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
		validarAlocacao(alocacao);
		return alocacaoRepository.save(alocacao);
	}

	private void validarAlocacao(Alocacao alocacao) throws NaoPossuiTempoDisponivelParaAlocacaoException, NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException {
		if (!hasTempoTrabalhadoSuficiente(alocacao))
			throw new NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException("Não é possível alocar um tempo maior que o tempo trabalhado no dia");
	private boolean hasTempoTrabalhadoSuficiente(Alocacao alocacao) {
		Duration horasTrabalhadas = obterHorasTrabalhadasNoDia(alocacao.getDia());
		Duration horasParaAlocar = alocacao.getTempo();
		return horasTrabalhadas.toMinutes() >= horasParaAlocar.toMinutes();
	}

	private Duration obterHorasTrabalhadasNoDia(LocalDate dia) {
		Optional<RegistroDeTrabalho> registroDeTrabalhoNoDia = registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(dia);
		return registroDeTrabalhoNoDia.isEmpty() ? Duration.ZERO : registroDeTrabalhoNoDia.get().getHorasTrabalhadas();
	}
}
