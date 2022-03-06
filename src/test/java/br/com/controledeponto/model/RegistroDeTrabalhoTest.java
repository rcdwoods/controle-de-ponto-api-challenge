package br.com.controledeponto.model;

class RegistroDeTrabalhoTest {

	@Test
	void deveRegistrarMomentoQuandoNaoExistiremMomentosRegistrados() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		Momento momentoASerRegistrado = new Momento(LocalDateTime.parse("2022-12-01T08:00:00"));
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(momentoASerRegistrado);
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados()).contains(momentoASerRegistrado);
	}

	@Test
	void deveRegistrarQuatroMomentosQuandoTodosMomentosForemValidos() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados()).hasSize(4);
	}

	@Test
	void deveRegistrarMomentoQuandoJaExistirUmMomentoMasADataHoraForInterior() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T09:00:00")));
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados().size()).isEqualTo(2);
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados().get(0).getDataHora()).isEqualTo("2022-12-01T08:00:00");
		Assertions.assertThat(registroDeTrabalho.getMomentosRegistrados().get(1).getDataHora()).isEqualTo("2022-12-01T09:00:00");
	}

	@Test
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoDataHoraForMenorDoQueOMomentoAnterior() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(HorarioInferiorAoUltimoRegistradoException.class, () -> {
			Momento momentoComHorarioMenor = new Momento(LocalDateTime.parse("2022-12-01T07:00:00"));
			registroDeTrabalho.registrarMomento(momentoComHorarioMenor);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Horário inferior ao último registrado.");
	}

	@Test
	void naoDeveRegistrarUmMomentoELancarUmaExceptionQuandoDataHoraJaTiverSidoRegistrada() throws HorarioInferiorAoUltimoRegistradoException, HorarioJaRegistradoException, NaoPodeHaverMaisDeQuatroRegistrosException, DeveHaverNoMinimoUmaHoraDeAlmocoException, NaoPodeRegistrarHorasEmFinalDeSemanaException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse("2022-12-01"));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T08:00:00")));

		Exception exception = org.junit.jupiter.api.Assertions.assertThrows(HorarioJaRegistradoException.class, () -> {
			Momento momentoComHorarioJaRegistrado = new Momento(LocalDateTime.parse("2022-12-01T08:00:00"));
			registroDeTrabalho.registrarMomento(momentoComHorarioJaRegistrado);
		});

		Assertions.assertThat(exception.getMessage()).isEqualTo("Horários já registrado");
	}

		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse("2022-12-01T18:00:00")));
}