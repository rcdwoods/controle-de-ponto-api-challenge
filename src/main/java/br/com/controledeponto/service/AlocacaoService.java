package br.com.controledeponto.service;

import br.com.controledeponto.exception.NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException;
import br.com.controledeponto.exception.NaoPossuiTempoDisponivelParaAlocacaoException;
import br.com.controledeponto.model.Alocacao;
import org.springframework.stereotype.Service;

@Service
public interface AlocacaoService {
	Alocacao alocarHoras(Alocacao alocacao);
}
