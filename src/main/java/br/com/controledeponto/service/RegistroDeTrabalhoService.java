package br.com.controledeponto.service;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;

import java.time.LocalDate;
import java.util.Optional;

public interface RegistroDeTrabalhoService {
	Optional<RegistroDeTrabalho> obterRegistroDeTrabalhoPorData(LocalDate data);
	RegistroDeTrabalho adicionarMomentoAoSeuRegistroDeTrabalho(Momento momento);
}
