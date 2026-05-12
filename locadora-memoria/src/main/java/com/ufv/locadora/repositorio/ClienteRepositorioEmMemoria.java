package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Cliente;

import java.util.Optional;

public class ClienteRepositorioEmMemoria
        extends RepositorioEmMemoria<Cliente>
        implements ClienteRepositorio {

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return dados.values().stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst();
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return dados.values().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return dados.values().stream().anyMatch(c -> c.getCpf().equals(cpf));
    }

    @Override
    public boolean existePorEmail(String email) {
        return dados.values().stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }
}
