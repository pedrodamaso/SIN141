package com.ufv.locadora.excecao;

/** Exceção lançada quando uma regra de negócio é violada. */
public class NegocioException extends RuntimeException {

    public NegocioException(String mensagem) {
        super(mensagem);
    }
}
