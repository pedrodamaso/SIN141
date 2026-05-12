package com.ufv.locadora.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LocacaoRequestDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "ID do veículo é obrigatório")
    private Long veiculoId;

    @NotNull(message = "Data de início é obrigatória")
    @FutureOrPresent(message = "Data de início deve ser hoje ou uma data futura")
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    @Future(message = "Data de fim deve ser uma data futura")
    private LocalDate dataFim;
}
