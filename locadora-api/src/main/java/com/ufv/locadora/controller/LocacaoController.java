package com.ufv.locadora.controller;

import com.ufv.locadora.dto.LocacaoRequestDTO;
import com.ufv.locadora.dto.LocacaoResponseDTO;
import com.ufv.locadora.service.LocacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locacoes")
@RequiredArgsConstructor
@Tag(name = "Locações", description = "Gerenciamento de locações de veículos")
public class LocacaoController {

    private final LocacaoService locacaoService;

    @GetMapping
    @Operation(summary = "Listar todas as locações")
    public ResponseEntity<List<LocacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(locacaoService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar locação por ID")
    public ResponseEntity<LocacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar locações de um cliente específico")
    public ResponseEntity<List<LocacaoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(locacaoService.listarPorCliente(clienteId));
    }

    @PostMapping
    @Operation(
        summary = "Realizar nova locação",
        description = "O valor total é calculado automaticamente com base no tipo de veículo: " +
                      "Carro = diária × dias | Moto = diária × dias × 0,90 (10% de desconto)"
    )
    public ResponseEntity<LocacaoResponseDTO> criar(@Valid @RequestBody LocacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locacaoService.criar(dto));
    }

    @PatchMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar locação ativa — libera o veículo")
    public ResponseEntity<LocacaoResponseDTO> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.finalizar(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar locação — libera o veículo")
    public ResponseEntity<LocacaoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(locacaoService.cancelar(id));
    }
}
