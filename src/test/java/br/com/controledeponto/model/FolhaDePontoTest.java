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

	private RegistroDeTrabalho criarRegistroDeTrabalhoCompleto(String data) throws NaoPodeHaverMaisDeQuatroRegistrosException, NaoPodeRegistrarHorasEmFinalDeSemanaException, HorarioInferiorAoUltimoRegistradoException, DeveHaverNoMinimoUmaHoraDeAlmocoException, HorarioJaRegistradoException {
		RegistroDeTrabalho registroDeTrabalho = new RegistroDeTrabalho(LocalDate.parse(data));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T09:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T12:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T13:00:00")));
		registroDeTrabalho.registrarMomento(new Momento(LocalDateTime.parse(data + "T18:00:00")));
		return registroDeTrabalho;
	}
}