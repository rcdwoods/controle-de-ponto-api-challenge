package br.com.controledeponto.model;

import br.com.controledeponto.constant.NumberConstant;
import br.com.controledeponto.exception.DeveHaverNoMinimoUmaHoraDeAlmocoException;
import br.com.controledeponto.exception.HorarioInferiorAoUltimoRegistradoException;
import br.com.controledeponto.exception.HorarioJaRegistradoException;
import br.com.controledeponto.exception.NaoPodeHaverMaisDeQuatroRegistrosException;
import br.com.controledeponto.exception.NaoPodeRegistrarHorasEmFinalDeSemanaException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class RegistroDeTrabalho {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "dia")
	private LocalDate dia;
	@OneToMany(mappedBy = "registroDeTrabalho")
	private List<Momento> momentosRegistrados = new ArrayList<>();
	private static final int JORNADA_DE_TRABALHO_EM_MINUTOS = 480;
	private static final int LIMITE_DE_REGISTROS = 4;

	public RegistroDeTrabalho(LocalDate dia) {
		this.dia = dia;
	}

	public RegistroDeTrabalho() { }

	public LocalDate getDia() {
		return dia;
	}

	public Duration getHorasTrabalhadas() {
		return getHorasDaPrimeiraEntradaESaida().plus(getHorasDaSegundaEntradaESaida());
	}

	public Duration getHorasExcedentes() {
		if (getHorasTrabalhadas().toMinutes() <= JORNADA_DE_TRABALHO_EM_MINUTOS) return Duration.ZERO;
		return getHorasTrabalhadas().minusMinutes(JORNADA_DE_TRABALHO_EM_MINUTOS);
	}

	public Duration getHorasDevidas() {
		if (getHorasTrabalhadas().toMinutes() >= JORNADA_DE_TRABALHO_EM_MINUTOS) return Duration.ZERO;
		return Duration.ofMinutes(JORNADA_DE_TRABALHO_EM_MINUTOS).minus(getHorasTrabalhadas());
	}

	public List<Momento> getMomentosRegistrados() {
		return this.momentosRegistrados;
	}

	public List<Momento> registrarMomento(Momento momento) {
		validarRegistroDeMomento(momento);
		this.momentosRegistrados.add(momento);
		return this.momentosRegistrados;
	}

	private Duration getHorasDaPrimeiraEntradaESaida() {
		if (momentosRegistrados.size() < NumberConstant.TWO) return Duration.ZERO;
		LocalDateTime primeiraEntrada = momentosRegistrados.get(NumberConstant.ZERO).getDataHora();
		LocalDateTime primeiraSaida = momentosRegistrados.get(NumberConstant.ONE).getDataHora();
		return Duration.between(primeiraEntrada, primeiraSaida);
	}

	private Duration getHorasDaSegundaEntradaESaida() {
		if (momentosRegistrados.size() < LIMITE_DE_REGISTROS) return Duration.ZERO;
		LocalDateTime primeiraEntrada = momentosRegistrados.get(NumberConstant.TWO).getDataHora();
		LocalDateTime primeiraSaida = momentosRegistrados.get(NumberConstant.THREE).getDataHora();
		return Duration.between(primeiraEntrada, primeiraSaida);
	}

	private void validarRegistroDeMomento(Momento momento) {
		if (hasMomentoRegistradoComHorarioSuperior(momento))
			throw new HorarioInferiorAoUltimoRegistradoException("Horário inferior ao último registrado");
		if (hasMomentoRegistradoComOMesmoHorario(momento))
			throw new HorarioJaRegistradoException("Horários já registrado");
		if (this.momentosRegistrados.size() == LIMITE_DE_REGISTROS)
			throw new NaoPodeHaverMaisDeQuatroRegistrosException("Apenas 4 horários podem ser registrados por dia");
		if (hasMenosHorasDeAlmocoDoQueOMinimo(momento))
			throw new DeveHaverNoMinimoUmaHoraDeAlmocoException("Deve haver no mínimo 1 hora de almoço");
		if (isMomentoEmUmFinalDeSemana(momento))
			throw new NaoPodeRegistrarHorasEmFinalDeSemanaException("Sábado e domingo não são permitidos como dia de trabalho");
	}

	private boolean isMomentoEmUmFinalDeSemana(Momento momento) {
		DayOfWeek diaDoMomento = momento.getDataHora().getDayOfWeek();
		return diaDoMomento == DayOfWeek.SATURDAY || diaDoMomento == DayOfWeek.SUNDAY;
	}

	private boolean hasMenosHorasDeAlmocoDoQueOMinimo(Momento momento) {
		if (this.momentosRegistrados.size() != NumberConstant.TWO) return false;
		Momento ultimoMomentoRegistrado = getUltimoMomentoRegistrado();
		long diferencaEmHorasEntreUltimoMomento = Duration.between(ultimoMomentoRegistrado.getDataHora(), momento.getDataHora()).toHours();
		return diferencaEmHorasEntreUltimoMomento == NumberConstant.ZERO;
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
		int posicaoDoUltimoMomento = this.momentosRegistrados.size() - NumberConstant.ONE;
		return this.momentosRegistrados
			.stream()
			.sorted(Comparator.comparing(Momento::getDataHora))
			.collect(Collectors.toList()).get(posicaoDoUltimoMomento);
	}
}
