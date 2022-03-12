package br.com.controledeponto.api;

import br.com.controledeponto.converter.MomentoConverter;
import br.com.controledeponto.service.BatidaService;
import io.swagger.api.BatidasApi;
import io.swagger.model.Momento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/batidas")
public class BatidaApiImpl implements BatidasApi {

	private BatidaService batidaService;

	private MomentoConverter momentoConverter;

	public BatidaApiImpl(BatidaService batidaService, MomentoConverter momentoConverter) {
		this.batidaService = batidaService;
		this.momentoConverter = momentoConverter;
	}

	@PostMapping
	public ResponseEntity<Momento> baterPonto(@Valid Momento momento) {
		br.com.controledeponto.model.Momento momentoConvertido = momentoConverter.converter(momento);
		Momento momentoSalvo = momentoConverter.converter(batidaService.salvarMomento(momentoConvertido));
		return ResponseEntity.status(HttpStatus.CREATED).body(momentoSalvo);
	}
}
