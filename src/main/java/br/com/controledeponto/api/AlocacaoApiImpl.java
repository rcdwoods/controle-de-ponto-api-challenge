package br.com.controledeponto.api;

import br.com.controledeponto.converter.AlocacaoConverter;
import br.com.controledeponto.service.AlocacaoService;
import io.swagger.api.AlocacoesApi;
import io.swagger.model.Alocacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/alocacoes")
public class AlocacaoApiImpl implements AlocacoesApi {

	private AlocacaoService alocacaoService;
	private AlocacaoConverter alocacaoConverter;

	public AlocacaoApiImpl(AlocacaoService alocacaoService, AlocacaoConverter alocacaoConverter) {
		this.alocacaoService = alocacaoService;
		this.alocacaoConverter = alocacaoConverter;
	}

	@PostMapping
	public ResponseEntity<Alocacao> alocarHoras(@Valid Alocacao alocacao) {
		br.com.controledeponto.model.Alocacao alocacaoConvertida = alocacaoConverter.converter(alocacao);
		br.com.controledeponto.model.Alocacao alocacaoSalva = alocacaoService.alocarHoras(alocacaoConvertida);
		return ResponseEntity.status(HttpStatus.CREATED).body(alocacaoConverter.converter(alocacaoSalva));
	}
}
