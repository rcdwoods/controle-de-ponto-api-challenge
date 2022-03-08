package br.com.controledeponto.service.impl;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.RegistroDeTrabalho;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.MomentoService;
import br.com.controledeponto.service.RegistroDeTrabalhoService;

public class MomentoServiceImpl implements MomentoService {

	private MomentoRepository momentoRepository;
	private RegistroDeTrabalhoService registroDeTrabalhoService;

	MomentoServiceImpl(MomentoRepository momentoRepository, RegistroDeTrabalhoService registroDeTrabalhoService) {
		this.momentoRepository = momentoRepository;
		this.registroDeTrabalhoService = registroDeTrabalhoService;
	}

	@Override
	public Momento salvarMomento(Momento momento) throws
		NaoPodeHaverMaisDeQuatroRegistrosException,
		NaoPodeRegistrarHorasEmFinalDeSemanaException,
		HorarioInferiorAoUltimoRegistradoException,
		DeveHaverNoMinimoUmaHoraDeAlmocoException,
		HorarioJaRegistradoException
	{
		RegistroDeTrabalho registroDeTrabalhoDoMomento = registroDeTrabalhoService.adicionarMomentoAoSeuRegistroDeTrabalho(momento);
		momento.setRegistroDeTrabalho(registroDeTrabalhoDoMomento);
		return momentoRepository.save(momento);
	}
}
