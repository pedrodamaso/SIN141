package com.ufv.locadora.model;

/**
 * Interface base para toda entidade do sistema.
 * Garante que todas as classes gerenciadas pelo repositório
 * genérico possuem um identificador.
 *
 * Conceito de OO: INTERFACE — define um contrato sem implementação.
 */
public interface Entidade {
    Long getId();
    void setId(Long id);
}
