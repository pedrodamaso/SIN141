package com.ufv.locadora.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ClienteResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private LocalDate dataCadastro;
}
