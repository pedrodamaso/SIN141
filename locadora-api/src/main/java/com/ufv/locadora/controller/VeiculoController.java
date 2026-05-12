package com.ufv.locadora.controller;

import com.ufv.locadora.dto.VeiculoRequestDTO;
import com.ufv.locadora.dto.VeiculoResponseDTO;
import com.ufv.locadora.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "Gerenciamento do frota de veículos — Carro e Moto (herança e polimorfismo)")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @GetMapping
    @Operation(summary = "Listar todos os veículos")
    public ResponseEntity<List<VeiculoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(veiculoService.listarTodos());
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar veículos disponíveis para locação")
    public ResponseEntity<List<VeiculoResponseDTO>> listarDisponiveis() {
        return ResponseEntity.ok(veiculoService.listarDisponiveis());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(
            @Parameter(description = "ID do veículo") @PathVariable Long id) {
        return ResponseEntity.ok(veiculoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar novo veículo",
        description = "Crie um CARRO (informe numeroPortas e tipoCombustivel) " +
                      "ou uma MOTO (informe cilindradas). O campo 'tipo' é obrigatório."
    )
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso")
    @ApiResponse(responseCode = "422", description = "Placa já cadastrada")
    public ResponseEntity<VeiculoResponseDTO> criar(@Valid @RequestBody VeiculoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um veículo")
    public ResponseEntity<VeiculoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VeiculoRequestDTO dto) {
        return ResponseEntity.ok(veiculoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover veículo (apenas se não estiver em locação ativa)")
    @ApiResponse(responseCode = "204", description = "Veículo removido")
    @ApiResponse(responseCode = "422", description = "Veículo em uso — não pode ser removido")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veiculoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
