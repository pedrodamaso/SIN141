package com.ufv.locadora.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * HERANÇA: Carro é um Veiculo com atributos e comportamento específicos.
 * POLIMORFISMO: sobrescreve calcularCustoLocacao() com regra própria.
 */
@Entity
@DiscriminatorValue("CARRO")
@Getter
@Setter
@NoArgsConstructor
public class Carro extends Veiculo {

    @Column(name = "numero_portas")
    private int numeroPortas;

    @Column(name = "tipo_combustivel", length = 20)
    private String tipoCombustivel;

    /**
     * Carros são cobrados pela diária cheia sem desconto.
     */
    @Override
    public BigDecimal calcularCustoLocacao(int numeroDias) {
        return getValorDiaria().multiply(BigDecimal.valueOf(numeroDias));
    }

    @Override
    public String getTipoVeiculo() {
        return "CARRO";
    }
}