package com.ufv.locadora.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * COMPOSIÇÃO: Locacao contém um Cliente e um Veiculo.
 * O valor total é calculado no construtor usando POLIMORFISMO:
 * veiculo.calcularCustoLocacao() chama a implementação correta
 * (Carro ou Moto) sem que Locacao precise saber qual é.
 */
public class Locacao implements Entidade {

    private Long id;
    private final Cliente cliente;
    private final Veiculo veiculo;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private final double valorTotal;
    private StatusLocacao status;

    public Locacao(Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim) {
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = StatusLocacao.ATIVA;

        int dias = (int) ChronoUnit.DAYS.between(dataInicio, dataFim);
        // POLIMORFISMO: Java decide em tempo de execução qual calcularCustoLocacao() chamar
        this.valorTotal = veiculo.calcularCustoLocacao(dias);
    }

    public int getNumeroDias() {
        return (int) ChronoUnit.DAYS.between(dataInicio, dataFim);
    }

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }

    public Veiculo getVeiculo() { return veiculo; }

    public LocalDate getDataInicio() { return dataInicio; }

    public LocalDate getDataFim() { return dataFim; }

    public double getValorTotal() { return valorTotal; }

    public StatusLocacao getStatus() { return status; }

    public void setStatus(StatusLocacao status) { this.status = status; }

    @Override
    public String toString() {
        return String.format(
                "Locacao[%d] %s -> %s %s | %s até %s (%d dias) | R$ %.2f | %s",
                id, cliente.getNome(), veiculo.getTipoVeiculo(), veiculo.getModelo(),
                dataInicio, dataFim, getNumeroDias(), valorTotal, status);
    }
}
