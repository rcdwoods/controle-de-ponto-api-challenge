package br.com.controledeponto.exception;

public class NaoHaRegistrosDeTrabalhoNoMesException extends RuntimeException {
	public NaoHaRegistrosDeTrabalhoNoMesException(String mensagem) {
		super(mensagem);
	}
}
