package com.ufv.locadora.model;

/**
 * ABSTRAÇÃO: classe abstrata — define o molde comum a todos os veículos.
 * Não pode ser instanciada diretamente (new Veiculo() é proibido).
 *
 * HERANÇA: Carro e Moto herdam desta classe.
 *
 * ENCAPSULAMENTO: todos os atributos são privados.
 * O acesso externo ocorre apenas pelos métodos públicos (getters/setters).
 */
public abstract class Veiculo implements Entidade {

    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private int ano;
    private double valorDiaria;
    private boolean disponivel;

    protected Veiculo(String placa, String marca, String modelo, int ano, double valorDiaria) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.valorDiaria = valorDiaria;
        this.disponivel = true;
    }

    // POLIMORFISMO: cada subclasse implementa este método com sua própria regra.
    public abstract double calcularCustoLocacao(int numeroDias);

    public abstract String getTipoVeiculo();

    // --- Getters e Setters (ENCAPSULAMENTO) ---

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }

    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAno() { return ano; }

    public void setAno(int ano) { this.ano = ano; }

    public double getValorDiaria() { return valorDiaria; }

    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    public boolean isDisponivel() { return disponivel; }

    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() {
        return String.format("[%s] id=%d | %s %s (%d) | Placa: %s | Diária: R$ %.2f | %s",
                getTipoVeiculo(), id, marca, modelo, ano, placa, valorDiaria,
                disponivel ? "Disponível" : "Em locação");
    }
}
