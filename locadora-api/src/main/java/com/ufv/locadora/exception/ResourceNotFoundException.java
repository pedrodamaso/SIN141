package com.ufv.locadora.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }

    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " com ID " + id + " não encontrado(a)");
    }
}
