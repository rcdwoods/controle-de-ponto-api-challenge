package br.com.controledeponto.exception;

public class NaoPossuiTempoDisponivelParaAlocacaoException extends RuntimeException {
	public NaoPossuiTempoDisponivelParaAlocacaoException(String mensagem) {
		super(mensagem);
	}
}
