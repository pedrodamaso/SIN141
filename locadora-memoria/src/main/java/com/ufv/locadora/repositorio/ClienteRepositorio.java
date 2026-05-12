package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Cliente;

import java.util.Optional;

public interface ClienteRepositorio extends Repositorio<Cliente> {

    Optional<Cliente> buscarPorCpf(String cpf);

    Optional<Cliente> buscarPorEmail(String email);

    boolean existePorCpf(String cpf);

    boolean existePorEmail(String email);
}
