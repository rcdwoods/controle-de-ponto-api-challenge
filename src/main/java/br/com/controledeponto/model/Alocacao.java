package br.com.controledeponto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDate;

@Entity
public class Alocacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "dia")
	private LocalDate dia;
	@Column(name = "tempo")
	private Duration tempo;
	@Column(name = "nome_projeto")
	private String nomeProjeto;

	public Alocacao(LocalDate dia, Duration tempo, String nomeProjeto) {
		this.dia = dia;
		this.tempo = tempo;
		this.nomeProjeto = nomeProjeto;
	}

	public Alocacao() { }

	public LocalDate getDia() {
		return dia;
	}

	public Duration getTempo() {
		return tempo;
	}
}
