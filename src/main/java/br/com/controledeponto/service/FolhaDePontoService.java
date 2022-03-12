package br.com.controledeponto.service;

import br.com.controledeponto.model.FolhaDePonto;

import java.time.YearMonth;

public interface FolhaDePontoService {
	FolhaDePonto gerarFolhaDePonto(YearMonth mes);
}
