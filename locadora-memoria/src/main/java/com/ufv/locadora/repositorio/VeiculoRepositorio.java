package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Veiculo;

import java.util.List;
import java.util.Optional;

/**
 * HERANÇA DE INTERFACE: estende o contrato genérico com operações
 * específicas de veículo. Demonstra como interfaces podem ser compostas.
 */
public interface VeiculoRepositorio extends Repositorio<Veiculo> {

    Optional<Veiculo> buscarPorPlaca(String placa);

    List<Veiculo> listarDisponiveis();

    boolean existePorPlaca(String placa);
}
