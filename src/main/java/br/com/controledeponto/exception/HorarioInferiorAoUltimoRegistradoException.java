package br.com.controledeponto.exception;

public class HorarioInferiorAoUltimoRegistradoException extends RuntimeException {
	public HorarioInferiorAoUltimoRegistradoException(String mensagem) {
		super(mensagem);
	}
}
