package br.com.controledeponto.api;

import br.com.controledeponto.converter.FolhaDePontoConverter;
import br.com.controledeponto.model.FolhaDePonto;
import br.com.controledeponto.service.FolhaDePontoService;
import io.swagger.api.FolhasDePontoApi;
import io.swagger.model.Relatorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.YearMonth;

@RestController
@RequestMapping("/v1/folhas-de-ponto")
public class FolhaDePontoApiImpl implements FolhasDePontoApi {

	@Autowired
	private FolhaDePontoService folhaDePontoService;
	@Autowired
	private FolhaDePontoConverter folhaDePontoConverter;

	@PostMapping("/{mes}")
	public ResponseEntity<Relatorio> gerarFolhaDePonto(@PathVariable @Valid String mes) {
		FolhaDePonto folhaDePontoDoMes = folhaDePontoService.gerarFolhaDePonto(YearMonth.parse(mes));
		return ResponseEntity.status(HttpStatus.CREATED).body(folhaDePontoConverter.converter(folhaDePontoDoMes));
	}
}
