package com.ufv.locadora.model;

/**
 * HERANÇA: Moto estende Veiculo.
 * POLIMORFISMO: implementa calcularCustoLocacao() com desconto de 10%.
 */
public class Moto extends Veiculo {

    private int cilindradas;

    public Moto(String placa, String marca, String modelo, int ano,
                double valorDiaria, int cilindradas) {
        super(placa, marca, modelo, ano, valorDiaria);
        this.cilindradas = cilindradas;
    }

    /** Motos têm 10% de desconto sobre o valor da diária. */
    @Override
    public double calcularCustoLocacao(int numeroDias) {
        return getValorDiaria() * numeroDias * 0.9;
    }

    @Override
    public String getTipoVeiculo() {
        return "MOTO";
    }

    public int getCilindradas() { return cilindradas; }

    public void setCilindradas(int cilindradas) { this.cilindradas = cilindradas; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | %d cc", cilindradas);
    }
}
