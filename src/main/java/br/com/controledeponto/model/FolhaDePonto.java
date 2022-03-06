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
		calcularHorasTrabalhadas();
		calcularHorasExcedentes();
	Duration getHorasTrabalhadas() { return this.horasTrabalhadas; }

	Duration getHorasExcedentes() { return this.horasExcedentes; }

	private void calcularHorasTrabalhadas() {
		this.horasTrabalhadas = this.registrosDeTrabalho
			.stream()
			.map(RegistroDeTrabalho::getHorasTrabalhadas)
			.reduce(Duration.ZERO, Duration::plus);
	}

	private void calcularHorasExcedentes() {
		this.horasExcedentes = this.registrosDeTrabalho
			.stream()
			.map(RegistroDeTrabalho::getHorasExcedentes)
			.reduce(Duration.ZERO, Duration::plus);
	}

}
