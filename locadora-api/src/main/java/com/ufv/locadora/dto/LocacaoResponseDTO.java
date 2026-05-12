package com.ufv.locadora.dto;

import com.ufv.locadora.model.StatusLocacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocacaoResponseDTO {
    private Long id;
    private Long clienteId;
    private String nomeCliente;
    private Long veiculoId;
    private String placaVeiculo;
    private String modeloVeiculo;
    private String tipoVeiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int numeroDias;
    private BigDecimal valorTotal;
    private StatusLocacao status;
}
