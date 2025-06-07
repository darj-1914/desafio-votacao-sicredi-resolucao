package br.com.votacao.client;

public interface CpfClient {
    boolean isCpfValido(String cpf);
    String verificarPermissaoVoto(String cpf);
}
