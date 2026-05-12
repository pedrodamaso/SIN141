package com.ufv.locadora.repositorio;

import java.util.List;
import java.util.Optional;

/**
 * INTERFACE GENÉRICA: define o contrato de persistência para qualquer tipo T.
 * Demonstra o uso de generics em Java combinado com interfaces.
 *
 * Todas as operações de armazenamento devem seguir este contrato,
 * independente de onde os dados ficam (memória, banco, arquivo, rede...).
 */
public interface Repositorio<T> {

    T salvar(T entidade);

    Optional<T> buscarPorId(Long id);

    List<T> listarTodos();

    boolean deletar(Long id);

    int contarTotal();
}
