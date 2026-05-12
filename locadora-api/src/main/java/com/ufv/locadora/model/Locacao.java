package com.ufv.locadora.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * COMPOSIÇÃO / ASSOCIAÇÃO: Locacao agrega Cliente e Veiculo.
 * Demonstra como objetos se relacionam — um locação pertence a
 * um cliente e envolve um veículo específico.
 */
@Entity
@Table(name = "locacoes")
@Getter
@Setter
@NoArgsConstructor
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusLocacao status = StatusLocacao.ATIVA;
}