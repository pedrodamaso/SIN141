package com.ufv.locadora;

import com.ufv.locadora.excecao.NegocioException;
import com.ufv.locadora.model.Carro;
import com.ufv.locadora.model.Cliente;
import com.ufv.locadora.model.Locacao;
import com.ufv.locadora.model.Moto;
import com.ufv.locadora.model.Veiculo;
import com.ufv.locadora.repositorio.ClienteRepositorioEmMemoria;
import com.ufv.locadora.repositorio.LocacaoRepositorioEmMemoria;
import com.ufv.locadora.repositorio.VeiculoRepositorioEmMemoria;
import com.ufv.locadora.servico.ClienteServico;
import com.ufv.locadora.servico.LocacaoServico;
import com.ufv.locadora.servico.VeiculoServico;

import java.time.LocalDate;
import java.util.List;

/**
 * Ponto de entrada da aplicação.
 *
 * Demonstra todos os conceitos de OO em sequência:
 * 1. Encapsulamento — acesso aos dados somente via métodos
 * 2. Abstração + Herança — Carro e Moto como especializações de Veiculo
 * 3. Polimorfismo — mesmo método, comportamentos diferentes
 * 4. Composição — Locacao contém Cliente e Veiculo
 * 5. Injeção de Dependência manual — serviços recebem repositórios
 */
public class Main {

    public static void main(String[] args) {

        // =====================================================================
        // CONFIGURAÇÃO — Injeção de Dependência Manual
        // Cada serviço recebe sua dependência pelo construtor (não cria internamente)
        // =====================================================================
        var veiculoRepositorio = new VeiculoRepositorioEmMemoria();
        var clienteRepositorio = new ClienteRepositorioEmMemoria();
        var locacaoRepositorio = new LocacaoRepositorioEmMemoria();

        var veiculoServico = new VeiculoServico(veiculoRepositorio);
        var clienteServico = new ClienteServico(clienteRepositorio);
        var locacaoServico = new LocacaoServico(locacaoRepositorio, veiculoServico, clienteServico);

        titulo("SISTEMA LOCADORA DE VEÍCULOS — Java Puro com Armazenamento em Memória");

        // =====================================================================
        // 1. HERANÇA + ENCAPSULAMENTO: criando objetos das subclasses
        // =====================================================================
        separador("1. Cadastro de Veículos (Herança + Encapsulamento)");

        // Carro e Moto são criados através de seus construtores específicos
        // O construtor de Veiculo é chamado via super() — herança em ação
        Carro carro = new Carro("ABC1D23", "Toyota", "Corolla", 2022, 150.00, 4, "FLEX");
        Moto moto = new Moto("XYZ5E67", "Honda", "CB 500", 2023, 80.00, 500);

        veiculoServico.salvar(carro);
        veiculoServico.salvar(moto);

        System.out.println("Veículos cadastrados:");
        veiculoServico.listarTodos().forEach(v -> System.out.println("  " + v));

        // =====================================================================
        // 2. ABSTRAÇÃO: referência do tipo pai aponta para objetos filhos
        // =====================================================================
        separador("2. Abstração — variável do tipo Veiculo referencia Carro ou Moto");

        List<Veiculo> frota = veiculoServico.listarTodos();
        for (Veiculo v : frota) {
            // Não sabemos aqui se é Carro ou Moto — estamos usando a abstração
            System.out.printf("  Tipo: %-5s | Modelo: %-15s | Disponível: %b%n",
                    v.getTipoVeiculo(), v.getModelo(), v.isDisponivel());
        }

        // =====================================================================
        // 3. POLIMORFISMO: mesmo método, resultados diferentes
        // =====================================================================
        separador("3. Polimorfismo — calcularCustoLocacao(5 dias)");

        int dias = 5;
        for (Veiculo v : frota) {
            double custo = v.calcularCustoLocacao(dias);
            System.out.printf("  %s %s: diária R$ %.2f × %d dias = R$ %.2f%n",
                    v.getTipoVeiculo(), v.getModelo(), v.getValorDiaria(), dias, custo);
        }
        System.out.println("  >> A Moto aplica 10% de desconto — mesmo método, comportamento diferente!");

        // =====================================================================
        // 4. ENCAPSULAMENTO: cliente — campos privados, acesso via getters
        // =====================================================================
        separador("4. Clientes (Encapsulamento)");

        Cliente maria = new Cliente("Maria Oliveira", "12345678901", "maria@email.com");
        maria.setTelefone("31987654321");
        maria.setDataNascimento(LocalDate.of(1995, 6, 15));

        Cliente joao = new Cliente("João Silva", "98765432100", "joao@email.com");

        clienteServico.salvar(maria);
        clienteServico.salvar(joao);

        System.out.println("Clientes cadastrados:");
        clienteServico.listarTodos().forEach(c -> System.out.println("  " + c));

        // =====================================================================
        // 5. COMPOSIÇÃO + POLIMORFISMO: Locacao usa Veiculo polimorficamente
        // =====================================================================
        separador("5. Locações (Composição + Polimorfismo)");

        LocalDate hoje = LocalDate.now();
        LocalDate daqui5dias = hoje.plusDays(5);

        Locacao locacaoCarro = locacaoServico.realizar(
                maria.getId(), carro.getId(), hoje, daqui5dias);
        System.out.println("Locação criada: " + locacaoCarro);

        Locacao locacaoMoto = locacaoServico.realizar(
                joao.getId(), moto.getId(), hoje, daqui5dias);
        System.out.println("Locação criada: " + locacaoMoto);

        System.out.println("\nVeículos disponíveis após locações:");
        var disponiveis = veiculoServico.listarDisponiveis();
        if (disponiveis.isEmpty()) {
            System.out.println("  Nenhum veículo disponível no momento");
        } else {
            disponiveis.forEach(v -> System.out.println("  " + v));
        }

        // =====================================================================
        // 6. TRATAMENTO DE EXCEÇÕES: regras de negócio
        // =====================================================================
        separador("6. Tratamento de Erros e Regras de Negócio");

        System.out.println("Tentando alugar o Corolla novamente (já locado)...");
        try {
            locacaoServico.realizar(joao.getId(), carro.getId(), hoje, daqui5dias);
        } catch (NegocioException e) {
            System.out.println("  ERRO ESPERADO: " + e.getMessage());
        }

        System.out.println("\nTentando cadastrar cliente com CPF duplicado...");
        try {
            clienteServico.salvar(new Cliente("Maria Cópia", "12345678901", "outroemail@test.com"));
        } catch (NegocioException e) {
            System.out.println("  ERRO ESPERADO: " + e.getMessage());
        }

        // =====================================================================
        // 7. CICLO COMPLETO: finalizar e verificar disponibilidade
        // =====================================================================
        separador("7. Finalizando Locação e Liberando Veículo");

        System.out.println("Antes: " + veiculoServico.buscarPorId(carro.getId()));

        locacaoServico.finalizar(locacaoCarro.getId());
        System.out.println("Locação do Corolla finalizada.");

        System.out.println("Depois: " + veiculoServico.buscarPorId(carro.getId()));

        // =====================================================================
        // 8. RESUMO FINAL
        // =====================================================================
        separador("Resumo do Sistema");
        System.out.printf("  Veículos: %d | Clientes: %d | Locações: %d%n",
                veiculoServico.listarTodos().size(),
                clienteServico.listarTodos().size(),
                locacaoServico.listarTodas().size());
        System.out.println("\n  Todas as locações:");
        locacaoServico.listarTodas().forEach(l -> System.out.println("  " + l));
    }

    // ---- Utilitários de formatação de saída ----

    private static void titulo(String texto) {
        String linha = "=".repeat(70);
        System.out.println("\n" + linha);
        System.out.println("  " + texto);
        System.out.println(linha);
    }

    private static void separador(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
    }
}
