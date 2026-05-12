package com.ufv.locadora.excecao;

/**
 * Exceção lançada quando um objeto buscado não existe no repositório.
 * Estende RuntimeException — não precisa ser declarada no throws.
 */
public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public EntidadeNaoEncontradaException(String tipo, Long id) {
        super(tipo + " com ID " + id + " não encontrado(a)");
    }
}
