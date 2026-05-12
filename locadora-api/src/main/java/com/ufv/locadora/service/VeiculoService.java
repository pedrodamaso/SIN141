package com.ufv.locadora.service;

import com.ufv.locadora.dto.VeiculoRequestDTO;
import com.ufv.locadora.dto.VeiculoResponseDTO;
import com.ufv.locadora.exception.BusinessException;
import com.ufv.locadora.exception.ResourceNotFoundException;
import com.ufv.locadora.model.Carro;
import com.ufv.locadora.model.Moto;
import com.ufv.locadora.model.Veiculo;
import com.ufv.locadora.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public List<VeiculoResponseDTO> listarTodos() {
        return veiculoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<VeiculoResponseDTO> listarDisponiveis() {
        return veiculoRepository.findByDisponivelTrue().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public VeiculoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidade(id));
    }

    @Transactional
    public VeiculoResponseDTO criar(VeiculoRequestDTO dto) {
        if (veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new BusinessException("Já existe um veículo cadastrado com a placa " + dto.getPlaca());
        }
        // POLIMORFISMO + FACTORY: o tipo correto é instanciado com base no campo "tipo"
        Veiculo veiculo = fabricarVeiculo(dto);
        return toResponseDTO(veiculoRepository.save(veiculo));
    }

    @Transactional
    public VeiculoResponseDTO atualizar(Long id, VeiculoRequestDTO dto) {
        Veiculo veiculo = buscarEntidade(id);

        if (veiculoRepository.existsByPlacaAndIdNot(dto.getPlaca(), id)) {
            throw new BusinessException("Já existe outro veículo com a placa " + dto.getPlaca());
        }

        veiculo.setPlaca(dto.getPlaca().toUpperCase());
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setValorDiaria(dto.getValorDiaria());

        // Pattern matching (Java 16+): verifica e faz cast com segurança
        if (veiculo instanceof Carro carro) {
            if (dto.getNumeroPortas() != null) carro.setNumeroPortas(dto.getNumeroPortas());
            if (dto.getTipoCombustivel() != null) carro.setTipoCombustivel(dto.getTipoCombustivel());
        } else if (veiculo instanceof Moto moto) {
            if (dto.getCilindradas() != null) moto.setCilindradas(dto.getCilindradas());
        }

        return toResponseDTO(veiculoRepository.save(veiculo));
    }

    @Transactional
    public void deletar(Long id) {
        Veiculo veiculo = buscarEntidade(id);
        if (!veiculo.isDisponivel()) {
            throw new BusinessException("Não é possível excluir um veículo com locação ativa");
        }
        veiculoRepository.delete(veiculo);
    }

    public Veiculo buscarEntidade(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));
    }

    // PADRÃO FACTORY METHOD: centraliza a criação de objetos do tipo correto
    private Veiculo fabricarVeiculo(VeiculoRequestDTO dto) {
        return switch (dto.getTipo().toUpperCase()) {
            case "CARRO" -> {
                Carro carro = new Carro();
                preencherBase(carro, dto);
                carro.setNumeroPortas(dto.getNumeroPortas() != null ? dto.getNumeroPortas() : 4);
                carro.setTipoCombustivel(dto.getTipoCombustivel() != null ? dto.getTipoCombustivel() : "FLEX");
                yield carro;
            }
            case "MOTO" -> {
                Moto moto = new Moto();
                preencherBase(moto, dto);
                moto.setCilindradas(dto.getCilindradas() != null ? dto.getCilindradas() : 150);
                yield moto;
            }
            default -> throw new BusinessException("Tipo de veículo inválido: " + dto.getTipo());
        };
    }

    private void preencherBase(Veiculo veiculo, VeiculoRequestDTO dto) {
        veiculo.setPlaca(dto.getPlaca().toUpperCase());
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setValorDiaria(dto.getValorDiaria());
        veiculo.setDisponivel(true);
    }

    // Converte a entidade (Veiculo) para DTO — usa pattern matching para os atributos específicos
    private VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {
        VeiculoResponseDTO.VeiculoResponseDTOBuilder builder = VeiculoResponseDTO.builder()
                .id(veiculo.getId())
                .tipo(veiculo.getTipoVeiculo())
                .placa(veiculo.getPlaca())
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .ano(veiculo.getAno())
                .valorDiaria(veiculo.getValorDiaria())
                .disponivel(veiculo.isDisponivel());

        if (veiculo instanceof Carro carro) {
            builder.numeroPortas(carro.getNumeroPortas())
                   .tipoCombustivel(carro.getTipoCombustivel());
        } else if (veiculo instanceof Moto moto) {
            builder.cilindradas(moto.getCilindradas());
        }

        return builder.build();
    }
}
