package com.ufv.locadora.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * ABSTRAÇÃO + HERANÇA
 * Classe abstrata que define o contrato comum a todos os veículos.
 * Nenhuma instância direta pode ser criada — apenas Carro e Moto.
 *
 * Mapeamento JPA com herança em tabela única (SINGLE_TABLE):
 * uma coluna discriminadora identifica o tipo real do objeto.
 */
@Entity
@Table(name = "veiculos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_veiculo", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ENCAPSULAMENTO: atributos privados expostos via getters/setters (Lombok)
    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int ano;

    @Column(name = "valor_diaria", nullable = false)
    private BigDecimal valorDiaria;

    @Column(nullable = false)
    private boolean disponivel = true;

    /**
     * POLIMORFISMO: método abstrato que cada subclasse implementa
     * com sua própria regra de negócio de cálculo de custo.
     */
    public abstract BigDecimal calcularCustoLocacao(int numeroDias);

    public abstract String getTipoVeiculo();
}
