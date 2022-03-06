package br.com.controledeponto.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
class MomentoTest {
	@Test
	void deveCriarUmMomentoComDataHora() {
		LocalDateTime dataHora = LocalDateTime.now();
		Momento momento = new Momento(dataHora);
		Assertions.assertThat(momento.getDataHora()).isEqualTo(dataHora);
	}
}
