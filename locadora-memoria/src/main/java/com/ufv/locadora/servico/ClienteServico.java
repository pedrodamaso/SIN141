package com.ufv.locadora.servico;

import com.ufv.locadora.excecao.EntidadeNaoEncontradaException;
import com.ufv.locadora.excecao.NegocioException;
import com.ufv.locadora.model.Cliente;
import com.ufv.locadora.repositorio.ClienteRepositorio;

import java.util.List;

public class ClienteServico {

    private final ClienteRepositorio repositorio;

    public ClienteServico(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Cliente salvar(Cliente cliente) {
        if (repositorio.existePorCpf(cliente.getCpf())) {
            throw new NegocioException("CPF " + cliente.getCpf() + " já está cadastrado");
        }
        if (repositorio.existePorEmail(cliente.getEmail())) {
            throw new NegocioException("E-mail " + cliente.getEmail() + " já está cadastrado");
        }
        return repositorio.salvar(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", id));
    }

    public List<Cliente> listarTodos() {
        return repositorio.listarTodos();
    }

    public Cliente atualizar(Cliente cliente) {
        buscarPorId(cliente.getId());
        return repositorio.salvar(cliente);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        repositorio.deletar(id);
    }
}
