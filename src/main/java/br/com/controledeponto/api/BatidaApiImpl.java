package br.com.controledeponto.api;

import io.swagger.api.BatidasApi;
import io.swagger.model.Momento;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batidas")
public class BatidasApiImpl implements BatidasApi {

	@Override
	public ResponseEntity<Momento> baterPonto(Momento momento) {
		return BatidasApi.super.baterPonto(momento);
	}
}
