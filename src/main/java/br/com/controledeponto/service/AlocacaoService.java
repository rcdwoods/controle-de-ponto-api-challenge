package br.com.controledeponto.service;

import br.com.controledeponto.model.Alocacao;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public interface AlocacaoService {
	Alocacao alocarHoras(Alocacao alocacao);
	List<Alocacao> obterAlocacoesPorMes(YearMonth mes);
}
