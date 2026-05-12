package com.ufv.locadora.repository;

import com.ufv.locadora.model.Locacao;
import com.ufv.locadora.model.StatusLocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    List<Locacao> findByClienteId(Long clienteId);

    List<Locacao> findByVeiculoId(Long veiculoId);

    List<Locacao> findByStatus(StatusLocacao status);
}