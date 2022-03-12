package br.com.controledeponto.service;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface RegistroDeTrabalhoService {
	Optional<RegistroDeTrabalho> obterRegistroDeTrabalhoPorData(LocalDate data);
	List<RegistroDeTrabalho> obterRegistrosDeTrabalhoPorMes(YearMonth mes);
	RegistroDeTrabalho adicionarMomentoAoSeuRegistroDeTrabalho(Momento momento);
}
