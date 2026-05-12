package com.ufv.locadora.servico;

import com.ufv.locadora.excecao.EntidadeNaoEncontradaException;
import com.ufv.locadora.excecao.NegocioException;
import com.ufv.locadora.model.Cliente;
import com.ufv.locadora.model.Locacao;
import com.ufv.locadora.model.StatusLocacao;
import com.ufv.locadora.model.Veiculo;
import com.ufv.locadora.repositorio.LocacaoRepositorio;

import java.time.LocalDate;
import java.util.List;

public class LocacaoServico {

    private final LocacaoRepositorio repositorio;
    private final VeiculoServico veiculoServico;
    private final ClienteServico clienteServico;

    public LocacaoServico(LocacaoRepositorio repositorio,
                          VeiculoServico veiculoServico,
                          ClienteServico clienteServico) {
        this.repositorio = repositorio;
        this.veiculoServico = veiculoServico;
        this.clienteServico = clienteServico;
    }

    public Locacao realizar(Long clienteId, Long veiculoId, LocalDate inicio, LocalDate fim) {
        if (!fim.isAfter(inicio)) {
            throw new NegocioException("A data de fim deve ser posterior à data de início");
        }

        Cliente cliente = clienteServico.buscarPorId(clienteId);
        Veiculo veiculo = veiculoServico.buscarPorId(veiculoId);

        if (!veiculo.isDisponivel()) {
            throw new NegocioException(
                    "O veículo " + veiculo.getPlaca() + " (" + veiculo.getModelo() + ") não está disponível");
        }

        // POLIMORFISMO: Locacao chama veiculo.calcularCustoLocacao() internamente
        Locacao locacao = new Locacao(cliente, veiculo, inicio, fim);

        veiculoServico.marcarComoIndisponivel(veiculoId);

        return repositorio.salvar(locacao);
    }

    public Locacao finalizar(Long locacaoId) {
        Locacao locacao = buscarPorId(locacaoId);

        if (locacao.getStatus() != StatusLocacao.ATIVA) {
            throw new NegocioException("Somente locações ativas podem ser finalizadas");
        }

        locacao.setStatus(StatusLocacao.FINALIZADA);
        veiculoServico.marcarComoDisponivel(locacao.getVeiculo().getId());

        return repositorio.salvar(locacao);
    }

    public Locacao cancelar(Long locacaoId) {
        Locacao locacao = buscarPorId(locacaoId);

        if (locacao.getStatus() == StatusLocacao.FINALIZADA) {
            throw new NegocioException("Locações já finalizadas não podem ser canceladas");
        }

        locacao.setStatus(StatusLocacao.CANCELADA);
        veiculoServico.marcarComoDisponivel(locacao.getVeiculo().getId());

        return repositorio.salvar(locacao);
    }

    public Locacao buscarPorId(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Locação", id));
    }

    public List<Locacao> listarTodas() {
        return repositorio.listarTodos();
    }

    public List<Locacao> listarPorCliente(Long clienteId) {
        return repositorio.buscarPorClienteId(clienteId);
    }

    public List<Locacao> listarAtivas() {
        return repositorio.buscarPorStatus(StatusLocacao.ATIVA);
    }
}
