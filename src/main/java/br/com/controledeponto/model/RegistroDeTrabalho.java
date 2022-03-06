package br.com.controledeponto.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeTrabalho {
	private LocalDate dia;
	private List<Momento> momentosRegistrados = new ArrayList<>();

	RegistroDeTrabalho(LocalDate dia) {
		this.dia = dia;
	}
}
