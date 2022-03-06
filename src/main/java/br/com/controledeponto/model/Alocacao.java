package br.com.controledeponto.model;

import java.time.Duration;
import java.time.LocalDate;

public class Alocacao {
	private LocalDate dia;
	private Duration tempo;
	private String nomeProjeto;

	Alocacao(LocalDate dia, Duration tempo, String nomeProjeto) {
		this.dia = dia;
		this.tempo = tempo;
		this.nomeProjeto = nomeProjeto;
	}
}
