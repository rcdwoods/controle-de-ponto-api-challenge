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
	}
	}
	}
	}
}
