package br.com.controledeponto.service.impl;

import br.com.controledeponto.exception.NaoHaRegistrosDeTrabalhoNoMesException;
import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.FolhaDePonto;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.service.AlocacaoService;
import br.com.controledeponto.service.FolhaDePontoService;
import br.com.controledeponto.service.RegistroDeTrabalhoService;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class FolhaDePontoServiceImpl implements FolhaDePontoService {

	private AlocacaoService alocacaoService;
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	public FolhaDePontoServiceImpl(AlocacaoService alocacaoService, RegistroDeTrabalhoService registroDeTrabalhoService) {
		this.alocacaoService = alocacaoService;
		this.registroDeTrabalhoService = registroDeTrabalhoService;
	}

	@Override
	public FolhaDePonto gerarFolhaDePonto(YearMonth mes) {
		List<RegistroDeTrabalho> registrosDoMes = registroDeTrabalhoService.obterRegistrosDeTrabalhoPorMes(mes);
		if (registrosDoMes.isEmpty()) throw new NaoHaRegistrosDeTrabalhoNoMesException("Não há registros de trabalho nesse mês para gerar um relatório.");
		List<Alocacao> alocacoesDoMes = alocacaoService.obterAlocacoesPorMes(mes);
		return new FolhaDePonto(mes, registrosDoMes, alocacoesDoMes);
	}
}
