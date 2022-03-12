package br.com.controledeponto.exception;

public class NaoPodeHaverMaisDeQuatroRegistrosException extends RuntimeException {
	public NaoPodeHaverMaisDeQuatroRegistrosException(String mensagem) {
		super(mensagem);
	}
}
