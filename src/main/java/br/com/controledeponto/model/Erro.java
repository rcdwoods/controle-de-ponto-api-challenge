package br.com.controledeponto.model;

public class Erro {
	private String mensagem;

	public Erro(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}
}
