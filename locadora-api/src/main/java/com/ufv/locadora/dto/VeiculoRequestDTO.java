package com.ufv.locadora.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VeiculoRequestDTO {

    @NotBlank(message = "Tipo é obrigatório")
    @Pattern(regexp = "CARRO|MOTO", message = "Tipo deve ser CARRO ou MOTO")
    private String tipo;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}[0-9][A-Z0-9][0-9]{2}", message = "Placa inválida (ex: ABC1D23 ou ABC1234)")
    private String placa;

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @Min(value = 1950, message = "Ano deve ser maior que 1950")
    @Max(value = 2030, message = "Ano inválido")
    private int ano;

    @NotNull(message = "Valor da diária é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser positivo")
    private BigDecimal valorDiaria;

    // Campos específicos de Carro (opcional para tipo MOTO)
    private Integer numeroPortas;
    private String tipoCombustivel;

    // Campos específicos de Moto (opcional para tipo CARRO)
    private Integer cilindradas;
}
