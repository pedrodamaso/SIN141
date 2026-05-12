package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Entidade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CLASSE ABSTRATA GENÉRICA: implementa as operações comuns de qualquer repositório
 * em memória, usando um Map como armazenamento interno.
 *
 * Conceitos demonstrados:
 * - HERANÇA + ABSTRAÇÃO: subclasses herdam o comportamento base
 * - GENERICS com bounded type: <T extends Entidade> garante que T tem getId/setId
 * - TEMPLATE METHOD: subclasses podem adicionar operações específicas
 * - ENCAPSULAMENTO: o Map interno é protegido (protected), não público
 *
 * LinkedHashMap preserva a ordem de inserção — útil para exibição consistente.
 */
public abstract class RepositorioEmMemoria<T extends Entidade> implements Repositorio<T> {

    protected final Map<Long, T> dados = new LinkedHashMap<>();
    private long proximoId = 1;

    @Override
    public T salvar(T entidade) {
        if (entidade.getId() == null) {
            entidade.setId(proximoId++);
        }
        dados.put(entidade.getId(), entidade);
        return entidade;
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public boolean deletar(Long id) {
        return dados.remove(id) != null;
    }

    @Override
    public int contarTotal() {
        return dados.size();
    }
}
