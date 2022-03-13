package br.com.controledeponto.service.impl;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.RegistroDeTrabalhoRepository;
import br.com.controledeponto.service.RegistroDeTrabalhoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroDeTrabalhoServiceImpl implements RegistroDeTrabalhoService {

	private static final int PRIMEIRO_DIA_DO_MES = 1;
	private RegistroDeTrabalhoRepository registroDeTrabalhoRepository;

	RegistroDeTrabalhoServiceImpl(RegistroDeTrabalhoRepository registroDeTrabalhoRepository) {
		this.registroDeTrabalhoRepository = registroDeTrabalhoRepository;
	}

	@Override
	public Optional<RegistroDeTrabalho> obterRegistroDeTrabalhoPorData(LocalDate data) {
		return registroDeTrabalhoRepository.findByDia(data);
	}

	@Override
	public List<RegistroDeTrabalho> obterRegistrosDeTrabalhoPorMes(YearMonth mes) {
		LocalDate primeiroDiaDoMes = mes.atDay(PRIMEIRO_DIA_DO_MES);
		LocalDate ultimoDiaDoMes = mes.atEndOfMonth();
		return registroDeTrabalhoRepository.findAllByDiaBetween(primeiroDiaDoMes, ultimoDiaDoMes);
	}

	@Override
	public RegistroDeTrabalho adicionarMomentoAoSeuRegistroDeTrabalho(Momento momento) {
		Optional<RegistroDeTrabalho> registroDeTrabalhoNoDia = obterRegistroDeTrabalhoPorData(momento.getDataHora().toLocalDate());

		if (registroDeTrabalhoNoDia.isEmpty())
			registroDeTrabalhoNoDia = Optional.of(criarRegistroDeTrabalho(new RegistroDeTrabalho(momento.getDataHora().toLocalDate())));

		registroDeTrabalhoNoDia.get().registrarMomento(momento);
		return registroDeTrabalhoRepository.save(registroDeTrabalhoNoDia.get());
	}

	private RegistroDeTrabalho criarRegistroDeTrabalho(RegistroDeTrabalho registroDeTrabalho) {
		return this.registroDeTrabalhoRepository.save(registroDeTrabalho);
	}
}
