package com.ufv.locadora.service;

import com.ufv.locadora.dto.LocacaoRequestDTO;
import com.ufv.locadora.dto.LocacaoResponseDTO;
import com.ufv.locadora.exception.BusinessException;
import com.ufv.locadora.exception.ResourceNotFoundException;
import com.ufv.locadora.model.Cliente;
import com.ufv.locadora.model.Locacao;
import com.ufv.locadora.model.StatusLocacao;
import com.ufv.locadora.model.Veiculo;
import com.ufv.locadora.repository.LocacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;

    public List<LocacaoResponseDTO> listarTodas() {
        return locacaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public LocacaoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidade(id));
    }

    public List<LocacaoResponseDTO> listarPorCliente(Long clienteId) {
        return locacaoRepository.findByClienteId(clienteId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public LocacaoResponseDTO criar(LocacaoRequestDTO dto) {
        if (!dto.getDataFim().isAfter(dto.getDataInicio())) {
            throw new BusinessException("Data de fim deve ser posterior à data de início");
        }

        Cliente cliente = clienteService.buscarEntidade(dto.getClienteId());
        Veiculo veiculo = veiculoService.buscarEntidade(dto.getVeiculoId());

        if (!veiculo.isDisponivel()) {
            throw new BusinessException("O veículo " + veiculo.getPlaca() + " não está disponível para locação");
        }

        int numeroDias = (int) ChronoUnit.DAYS.between(dto.getDataInicio(), dto.getDataFim());

        // POLIMORFISMO em ação: o método correto é chamado em tempo de execução
        // dependendo se veiculo é um Carro (sem desconto) ou Moto (10% de desconto)
        BigDecimal valorTotal = veiculo.calcularCustoLocacao(numeroDias);

        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setVeiculo(veiculo);
        locacao.setDataInicio(dto.getDataInicio());
        locacao.setDataFim(dto.getDataFim());
        locacao.setValorTotal(valorTotal);
        locacao.setStatus(StatusLocacao.ATIVA);

        veiculo.setDisponivel(false);

        return toResponseDTO(locacaoRepository.save(locacao));
    }

    @Transactional
    public LocacaoResponseDTO finalizar(Long id) {
        Locacao locacao = buscarEntidade(id);

        if (locacao.getStatus() != StatusLocacao.ATIVA) {
            throw new BusinessException("Somente locações ativas podem ser finalizadas");
        }

        locacao.setStatus(StatusLocacao.FINALIZADA);
        locacao.getVeiculo().setDisponivel(true);

        return toResponseDTO(locacaoRepository.save(locacao));
    }

    @Transactional
    public LocacaoResponseDTO cancelar(Long id) {
        Locacao locacao = buscarEntidade(id);

        if (locacao.getStatus() == StatusLocacao.FINALIZADA) {
            throw new BusinessException("Locações já finalizadas não podem ser canceladas");
        }

        locacao.setStatus(StatusLocacao.CANCELADA);
        locacao.getVeiculo().setDisponivel(true);

        return toResponseDTO(locacaoRepository.save(locacao));
    }

    private Locacao buscarEntidade(Long id) {
        return locacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locação", id));
    }

    private LocacaoResponseDTO toResponseDTO(Locacao locacao) {
        int numeroDias = (int) ChronoUnit.DAYS.between(
                locacao.getDataInicio(), locacao.getDataFim());

        return LocacaoResponseDTO.builder()
                .id(locacao.getId())
                .clienteId(locacao.getCliente().getId())
                .nomeCliente(locacao.getCliente().getNome())
                .veiculoId(locacao.getVeiculo().getId())
                .placaVeiculo(locacao.getVeiculo().getPlaca())
                .modeloVeiculo(locacao.getVeiculo().getModelo())
                .tipoVeiculo(locacao.getVeiculo().getTipoVeiculo())
                .dataInicio(locacao.getDataInicio())
                .dataFim(locacao.getDataFim())
                .numeroDias(numeroDias)
                .valorTotal(locacao.getValorTotal())
                .status(locacao.getStatus())
                .build();
    }
}
