package br.com.controledeponto.model;

import java.time.Duration;
import java.time.YearMonth;
import java.util.List;

public class FolhaDePonto {
	private YearMonth mes;
	private Duration horasTrabalhadas;
	private Duration horasExcedentes;
	private Duration horasDevidas;
	private List<RegistroDeTrabalho> registrosDeTrabalho;
	private List<Alocacao> alocacoes;

	FolhaDePonto(YearMonth mes, List<RegistroDeTrabalho> registrosDeTrabalho, List<Alocacao> alocacoes) {
		this.mes = mes;
		this.registrosDeTrabalho = registrosDeTrabalho;
		this.alocacoes = alocacoes;
	}
}
