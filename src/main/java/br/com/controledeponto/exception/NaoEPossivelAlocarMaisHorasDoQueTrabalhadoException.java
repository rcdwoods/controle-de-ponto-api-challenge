package br.com.controledeponto.exception;

public class NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException extends RuntimeException {
	public NaoEPossivelAlocarMaisHorasDoQueTrabalhadoException(String mensagem) {
		super(mensagem);
	}
}
