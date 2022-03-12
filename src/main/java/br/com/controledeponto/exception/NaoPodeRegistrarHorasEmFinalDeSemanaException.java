package br.com.controledeponto.exception;

public class NaoPodeRegistrarHorasEmFinalDeSemanaException extends RuntimeException {
	public NaoPodeRegistrarHorasEmFinalDeSemanaException(String mensagem) {
		super(mensagem);
	}
}
