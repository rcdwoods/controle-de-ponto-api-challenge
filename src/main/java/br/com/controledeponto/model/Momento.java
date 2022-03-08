package br.com.controledeponto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Momento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "data_hora")
	private LocalDateTime dataHora;
	@ManyToOne
	@JoinColumn(name = "registro_de_trabalho_id")
	private RegistroDeTrabalho registroDeTrabalho;

	public Momento(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	public Momento() {
	}

	public LocalDateTime getDataHora() {
		return this.dataHora;
	}

	public RegistroDeTrabalho getRegistroDeTrabalho() {
		return this.registroDeTrabalho;
	}

	public void setRegistroDeTrabalho(RegistroDeTrabalho registroDeTrabalho) {
		this.registroDeTrabalho = registroDeTrabalho;
	}
}
