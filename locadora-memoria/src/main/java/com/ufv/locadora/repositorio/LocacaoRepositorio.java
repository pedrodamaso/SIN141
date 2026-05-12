package com.ufv.locadora.repositorio;

import com.ufv.locadora.model.Locacao;
import com.ufv.locadora.model.StatusLocacao;

import java.util.List;

public interface LocacaoRepositorio extends Repositorio<Locacao> {

    List<Locacao> buscarPorClienteId(Long clienteId);

    List<Locacao> buscarPorStatus(StatusLocacao status);
}
