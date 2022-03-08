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
		calcularHorasDevidas();
	}

	Duration getHorasTrabalhadas() {
		return this.horasTrabalhadas;
	}

	Duration getHorasExcedentes() {
		return this.horasExcedentes;
	}

	Duration getHorasDevidas() {
		return this.horasDevidas;
	}

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

	private void calcularHorasDevidas() {
		this.horasDevidas = this.registrosDeTrabalho
			.stream()
			.map(RegistroDeTrabalho::getHorasDevidas)
			.reduce(Duration.ZERO, Duration::plus);
	}
}
