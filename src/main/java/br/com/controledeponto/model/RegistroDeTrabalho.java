package br.com.controledeponto.model;

import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegistroDeTrabalho {
	private LocalDate dia;
	private List<Momento> momentosRegistrados = new ArrayList<>();

	RegistroDeTrabalho(LocalDate dia) {
		this.dia = dia;
	}

	List<Momento> getMomentosRegistrados() {
		return this.momentosRegistrados;
	}

	List<Momento> registrarMomento(Momento momento) throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		validarRegistroDeMomento(momento);
		this.momentosRegistrados.add(momento);
		return this.momentosRegistrados;
	}
	private void validarRegistroDeMomento(Momento momento) throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		if (hasMomentoRegistradoComHorarioSuperior(momento))
			throw new HorarioInferiorAoUltimoRegistradoException("Horário inferior ao último registrado.");
		if (hasMomentoRegistradoComOMesmoHorario(momento))
			throw new HorarioJaRegistradoException("Horários já registrado");
		if (this.momentosRegistrados.size() == 4)
			throw new NaoPodeHaverMaisDeQuatroRegistrosException("Apenas 4 horários podem ser registrados por dia");
		if (hasMenosHorasDeAlmocoDoQueOMinimo(momento))
			throw new DeveHaverNoMinimoUmaHoraDeAlmocoException("Deve haver no mínimo 1 hora de almoço");

	private boolean hasMenosHorasDeAlmocoDoQueOMinimo(Momento momento) {
		if (this.momentosRegistrados.size() != 2) return false;
		Momento ultimoMomentoRegistrado = getUltimoMomentoRegistrado();
		long diferencaEmHorasEntreUltimoMomento =
			Duration.between(ultimoMomentoRegistrado.getDataHora(), momento.getDataHora()).toHours();
		return diferencaEmHorasEntreUltimoMomento == 0;
	}

	private boolean hasMomentoRegistradoComOMesmoHorario(Momento momento) {
		return this.momentosRegistrados
			.stream()
			.anyMatch(momentoRegistrado -> momentoRegistrado.getDataHora().isEqual(momento.getDataHora()));
	}

	private boolean hasMomentoRegistradoComHorarioSuperior(Momento momento) {
		Momento ultimoMomentoRegistrado = getUltimoMomentoRegistrado();
		if (ultimoMomentoRegistrado == null) return false;
		return ultimoMomentoRegistrado.getDataHora().isAfter(momento.getDataHora());
	}

	private Momento getUltimoMomentoRegistrado() {
		if (momentosRegistrados.isEmpty()) return null;
		int posicaoDoUltimoMomento = this.momentosRegistrados.size() - 1;
		return this.momentosRegistrados
			.stream()
			.sorted(Comparator.comparing(momento -> momento.getDataHora()))
			.collect(Collectors.toList()).get(posicaoDoUltimoMomento);
	}
}
