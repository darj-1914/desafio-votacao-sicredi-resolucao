package br.com.votacao.client;

import br.com.votacao.exception.CpfNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FakeCpfClient implements CpfClient {

    private Random random = new Random();

    @Override
    public boolean isCpfValido(String cpf) {
        return random.nextDouble() < 0.7;
    }

    @Override
    public String verificarPermissaoVoto(String cpf) {
        if (!isCpfValido(cpf)) {
            throw new CpfNotFoundException("CPF inválido");
        }
        return random.nextBoolean() ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";
    }
}