package com.ufv.locadora.model;

/**
 * HERANÇA: Carro estende Veiculo — herda todos os atributos e métodos.
 * POLIMORFISMO: implementa calcularCustoLocacao() com regra específica de carro.
 */
public class Carro extends Veiculo {

    private int numeroPortas;
    private String tipoCombustivel;

    public Carro(String placa, String marca, String modelo, int ano,
                 double valorDiaria, int numeroPortas, String tipoCombustivel) {
        super(placa, marca, modelo, ano, valorDiaria);
        this.numeroPortas = numeroPortas;
        this.tipoCombustivel = tipoCombustivel;
    }

    /** Carros são cobrados pelo valor cheio da diária. */
    @Override
    public double calcularCustoLocacao(int numeroDias) {
        return getValorDiaria() * numeroDias;
    }

    @Override
    public String getTipoVeiculo() {
        return "CARRO";
    }

    public int getNumeroPortas() { return numeroPortas; }

    public void setNumeroPortas(int numeroPortas) { this.numeroPortas = numeroPortas; }

    public String getTipoCombustivel() { return tipoCombustivel; }

    public void setTipoCombustivel(String tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | %d portas | %s", numeroPortas, tipoCombustivel);
    }
}
