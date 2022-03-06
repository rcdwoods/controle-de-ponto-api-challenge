package br.com.controledeponto.model;

import java.time.LocalDateTime;

public class Momento {
	private LocalDateTime dataHora;

	Momento(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}

	LocalDateTime getDataHora() {
		return this.dataHora;
	}
}
