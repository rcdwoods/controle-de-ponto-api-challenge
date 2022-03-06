package br.com.controledeponto.model;

class FolhaDePontoTest {

	@Test
	void deveCalcularHorasTotaisTrabalhadasNoMes() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoCompleto("2021-01-04"),
			criarRegistroDeTrabalhoCompleto("2021-01-05"),
			criarRegistroDeTrabalhoCompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasTrabalhadas()).isEqualTo(Duration.parse("PT24H0M0S"));
	}

	@Test
	void deveGerarHorasExcedentesQuandoFuncionarioTrabalharAMais() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoExcedente("2021-01-04"),
			criarRegistroDeTrabalhoExcedente("2021-01-05"),
			criarRegistroDeTrabalhoExcedente("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasExcedentes()).isEqualTo(Duration.parse("PT1H30M0S"));
	}

	@Test
	void naoDeveGerarHorasExcedentesQuandoFuncionarioTrabalharHorarioNormal() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoCompleto("2021-01-04"),
			criarRegistroDeTrabalhoCompleto("2021-01-05"),
			criarRegistroDeTrabalhoCompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasExcedentes()).isEqualTo(Duration.parse("PT0S"));
	}

	@Test
	void naoDeveGerarHorasExcedentesQuandoFuncionarioTrabalharHorasAMenos() throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		List<RegistroDeTrabalho> registrosDeTrabalho = List.of(
			criarRegistroDeTrabalhoIncompleto("2021-01-04"),
			criarRegistroDeTrabalhoIncompleto("2021-01-05"),
			criarRegistroDeTrabalhoIncompleto("2021-01-06")
		);
		FolhaDePonto folhaDePonto = new FolhaDePonto(YearMonth.parse("2021-01"), registrosDeTrabalho, List.of());
		Assertions.assertThat(folhaDePonto.getHorasExcedentes()).isEqualTo(Duration.parse("PT0S"));
	}
	private RegistroDeTrabalho criarRegistroDeTrabalhoCompleto(String data) throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T18:00:00")));
		return registroDeTrabalho;
	}

	private RegistroDeTrabalho criarRegistroDeTrabalhoExcedente(String data) throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T18:30:00")));
		return registroDeTrabalho;
	}

}