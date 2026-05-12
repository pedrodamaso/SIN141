package com.ufv.locadora.service;

import com.ufv.locadora.dto.ClienteRequestDTO;
import com.ufv.locadora.dto.ClienteResponseDTO;
import com.ufv.locadora.exception.BusinessException;
import com.ufv.locadora.exception.ResourceNotFoundException;
import com.ufv.locadora.model.Cliente;
import com.ufv.locadora.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidade(id));
    }

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("CPF " + dto.getCpf() + " já está cadastrado");
        }
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail " + dto.getEmail() + " já está cadastrado");
        }
        Cliente cliente = new Cliente();
        preencherDados(cliente, dto);
        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarEntidade(id);

        if (clienteRepository.existsByCpfAndIdNot(dto.getCpf(), id)) {
            throw new BusinessException("CPF " + dto.getCpf() + " pertence a outro cliente");
        }
        if (clienteRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("E-mail " + dto.getEmail() + " pertence a outro cliente");
        }

        preencherDados(cliente, dto);
        return toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }

    public Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private void preencherDados(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setDataNascimento(dto.getDataNascimento());
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(cliente.getCpf())
                .email(cliente.getEmail())
                .telefone(cliente.getTelefone())
                .dataNascimento(cliente.getDataNascimento())
                .dataCadastro(cliente.getDataCadastro())
                .build();
    }
}
