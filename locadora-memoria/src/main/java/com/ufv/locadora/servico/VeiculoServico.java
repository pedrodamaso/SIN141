package com.ufv.locadora.servico;

import com.ufv.locadora.excecao.EntidadeNaoEncontradaException;
import com.ufv.locadora.excecao.NegocioException;
import com.ufv.locadora.model.Veiculo;
import com.ufv.locadora.repositorio.VeiculoRepositorio;

import java.util.List;

/**
 * Camada de serviço: contém as regras de negócio para veículos.
 *
 * INJEÇÃO DE DEPENDÊNCIA MANUAL: o repositório é recebido pelo construtor,
 * não criado internamente. Isso permite trocar a implementação (memória,
 * banco, arquivo) sem alterar o serviço — Princípio da Inversão de Dependência.
 */
public class VeiculoServico {

    private final VeiculoRepositorio repositorio;

    public VeiculoServico(VeiculoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Veiculo salvar(Veiculo veiculo) {
        if (repositorio.existePorPlaca(veiculo.getPlaca())) {
            throw new NegocioException("Já existe um veículo com a placa " + veiculo.getPlaca());
        }
        return repositorio.salvar(veiculo);
    }

    public Veiculo buscarPorId(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo", id));
    }

    public List<Veiculo> listarTodos() {
        return repositorio.listarTodos();
    }

    public List<Veiculo> listarDisponiveis() {
        return repositorio.listarDisponiveis();
    }

    public Veiculo atualizar(Veiculo veiculo) {
        buscarPorId(veiculo.getId()); // garante que existe
        return repositorio.salvar(veiculo);
    }

    public void deletar(Long id) {
        Veiculo veiculo = buscarPorId(id);
        if (!veiculo.isDisponivel()) {
            throw new NegocioException("Não é possível remover um veículo com locação ativa");
        }
        repositorio.deletar(id);
    }

    // Usado internamente pelos outros serviços para alterar disponibilidade
    public void marcarComoIndisponivel(Long veiculoId) {
        Veiculo veiculo = buscarPorId(veiculoId);
        veiculo.setDisponivel(false);
        repositorio.salvar(veiculo);
    }

    public void marcarComoDisponivel(Long veiculoId) {
        Veiculo veiculo = buscarPorId(veiculoId);
        veiculo.setDisponivel(true);
        repositorio.salvar(veiculo);
    }
}
