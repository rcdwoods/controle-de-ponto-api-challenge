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

	public FolhaDePonto(YearMonth mes, List<RegistroDeTrabalho> registrosDeTrabalho, List<Alocacao> alocacoes) {
		this.mes = mes;
		this.registrosDeTrabalho = registrosDeTrabalho;
		this.alocacoes = alocacoes;
		calcularHorasTrabalhadas();
		calcularHorasExcedentes();
		calcularHorasDevidas();
	}

	public YearMonth getMes() {
		return this.mes;
	}

	public Duration getHorasTrabalhadas() {
		return this.horasTrabalhadas;
	}

	public Duration getHorasExcedentes() {
		return this.horasExcedentes;
	}

	public Duration getHorasDevidas() {
		return this.horasDevidas;
	}

	public List<RegistroDeTrabalho> getRegistrosDeTrabalho() {
		return this.registrosDeTrabalho;
	}

	public List<Alocacao> getAlocacoes() {
		return this.alocacoes;
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
