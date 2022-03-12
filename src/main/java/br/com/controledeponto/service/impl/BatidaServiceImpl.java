package br.com.controledeponto.service.impl;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.BatidaService;
import br.com.controledeponto.service.RegistroDeTrabalhoService;
import org.springframework.stereotype.Service;

@Service
public class BatidaServiceImpl implements BatidaService {

	private MomentoRepository momentoRepository;
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	BatidaServiceImpl(MomentoRepository momentoRepository, RegistroDeTrabalhoService registroDeTrabalhoService) {
		this.momentoRepository = momentoRepository;
		this.registroDeTrabalhoService = registroDeTrabalhoService;
	}

	@Override
	public Momento salvarMomento(Momento momento) {
		RegistroDeTrabalho registroDeTrabalhoDoMomento = registroDeTrabalhoService.adicionarMomentoAoSeuRegistroDeTrabalho(momento);
		momento.setRegistroDeTrabalho(registroDeTrabalhoDoMomento);
		return momentoRepository.save(momento);
	}
}
