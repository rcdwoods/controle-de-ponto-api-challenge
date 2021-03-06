package br.com.controledeponto.service.impl;

import br.com.controledeponto.exception.NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException;
import br.com.controledeponto.exception.NaoPossuiTempoDisponivelParaAlocacaoException;
import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.AlocacaoRepository;
import br.com.controledeponto.service.AlocacaoService;
import br.com.controledeponto.service.RegistroDeTrabalhoService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class AlocacaoServiceImpl implements AlocacaoService {

	private static final int PRIMEIRO_DIA_DO_MES = 1;
	private AlocacaoRepository alocacaoRepository;
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	AlocacaoServiceImpl(AlocacaoRepository alocacaoRepository, RegistroDeTrabalhoService registroDeTrabalhoService) {
		this.alocacaoRepository = alocacaoRepository;
		this.registroDeTrabalhoService = registroDeTrabalhoService;
	}

	@Override
	public Alocacao alocarHoras(Alocacao alocacao) {
		validarAlocacao(alocacao);
		Optional<Alocacao> alocacaoExistenteNoProjeto = alocacaoRepository.findByDiaAndNomeProjeto(alocacao.getDia(), alocacao.getNomeProjeto());
		alocacaoExistenteNoProjeto.ifPresent(alocacaoExistente -> alocacaoExistente.setTempo(alocacaoExistente.getTempo().plus(alocacao.getTempo())));
		return alocacaoRepository.save(alocacaoExistenteNoProjeto.orElse(alocacao));
	}

	@Override
	public List<Alocacao> obterAlocacoesPorMes(YearMonth mes) {
		LocalDate primeiroDiaDoMes = mes.atDay(PRIMEIRO_DIA_DO_MES);
		LocalDate ultimoDiaDoMes = mes.atEndOfMonth();
		return alocacaoRepository.findAllByDiaBetween(primeiroDiaDoMes, ultimoDiaDoMes);
	}

	private void validarAlocacao(Alocacao alocacao) {
		if (!hasTempoTrabalhadoSuficiente(alocacao))
			throw new NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException("N??o pode alocar tempo maior que o tempo trabalhado no dia");
		if (!hasHorasNaoAlocadasSuficiente(alocacao))
			throw new NaoPossuiTempoDisponivelParaAlocacaoException("N??o possui tempo dispon??vel para aloca????o");
	}

	private boolean hasTempoTrabalhadoSuficiente(Alocacao alocacao) {
		Duration horasTrabalhadas = obterHorasTrabalhadasNoDia(alocacao.getDia());
		Duration horasParaAlocar = alocacao.getTempo();
		return horasTrabalhadas.toSeconds() >= horasParaAlocar.toSeconds();
	}

	private boolean hasHorasNaoAlocadasSuficiente(Alocacao alocacao) {
		List<Alocacao> alocacoesNoDia = obterAlocacoesNoDia(alocacao.getDia());
		Duration horasTrabalhadasNoDia = obterHorasTrabalhadasNoDia(alocacao.getDia());
		Duration horasAlocadasEmProjetos = alocacoesNoDia.stream().map(Alocacao::getTempo).reduce(Duration.ZERO, Duration::plus);
		Duration horasDisponiveisParaAlocacao = horasTrabalhadasNoDia.minus(horasAlocadasEmProjetos);
		Duration horasParaAlocar = alocacao.getTempo();
		return horasDisponiveisParaAlocacao.toSeconds() >= horasParaAlocar.toSeconds();
	}

	private List<Alocacao> obterAlocacoesNoDia(LocalDate dia) {
		return this.alocacaoRepository.findAllByDia(dia);
	}

	private Duration obterHorasTrabalhadasNoDia(LocalDate dia) {
		Optional<RegistroDeTrabalho> registroDeTrabalhoNoDia = registroDeTrabalhoService.obterRegistroDeTrabalhoPorData(dia);
		return registroDeTrabalhoNoDia.isEmpty() ? Duration.ZERO : registroDeTrabalhoNoDia.get().getHorasTrabalhadas();
	}
}
