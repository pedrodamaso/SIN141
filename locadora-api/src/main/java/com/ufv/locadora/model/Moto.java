package com.ufv.locadora.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * HERANÇA: Moto é um Veiculo com atributos e comportamento específicos.
 * POLIMORFISMO: sobrescreve calcularCustoLocacao() — motos têm 10% de desconto.
 */
@Entity
@DiscriminatorValue("MOTO")
@Getter
@Setter
@NoArgsConstructor
public class Moto extends Veiculo {

    @Column
    private int cilindradas;

    /**
     * Motos possuem desconto de 10% sobre o valor da diária.
     */
    @Override
    public BigDecimal calcularCustoLocacao(int numeroDias) {
        return getValorDiaria()
                .multiply(BigDecimal.valueOf(numeroDias))
                .multiply(BigDecimal.valueOf(0.9));
    }

    @Override
    public String getTipoVeiculo() {
        return "MOTO";
    }
}
