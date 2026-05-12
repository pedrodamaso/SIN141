package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Locacao;
import com.ufv.locadora.model.StatusLocacao;

import java.util.List;
import java.util.stream.Collectors;

public class LocacaoRepositorioEmMemoria
        extends RepositorioEmMemoria<Locacao>
        implements LocacaoRepositorio {

    @Override
    public List<Locacao> buscarPorClienteId(Long clienteId) {
        return dados.values().stream()
                .filter(l -> l.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Locacao> buscarPorStatus(StatusLocacao status) {
        return dados.values().stream()
                .filter(l -> l.getStatus() == status)
                .collect(Collectors.toList());
    }
}
