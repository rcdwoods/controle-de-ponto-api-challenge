package br.com.controledeponto.exception;

public class HorarioJaRegistradoException extends RuntimeException {
	public HorarioJaRegistradoException(String mensagem) {
		super(mensagem);
	}
}
