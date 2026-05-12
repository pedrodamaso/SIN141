package com.ufv.locadora.repository;

import com.ufv.locadora.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    List<Veiculo> findByDisponivelTrue();

    Optional<Veiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, Long id);
}