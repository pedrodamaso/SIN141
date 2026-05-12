package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Veiculo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HERANÇA MÚLTIPLA DE CONTRATO: estende RepositorioEmMemoria (herança de classe)
 * e implementa VeiculoRepositorio (contrato de interface).
 * Herda salvar/buscar/listar/deletar e adiciona operações específicas.
 */
public class VeiculoRepositorioEmMemoria
        extends RepositorioEmMemoria<Veiculo>
        implements VeiculoRepositorio {

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return dados.values().stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst();
    }

    @Override
    public List<Veiculo> listarDisponiveis() {
        return dados.values().stream()
                .filter(Veiculo::isDisponivel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorPlaca(String placa) {
        return dados.values().stream()
                .anyMatch(v -> v.getPlaca().equalsIgnoreCase(placa));
    }
}
