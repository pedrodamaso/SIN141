package com.ufv.locadora.model;

import java.time.LocalDate;

/**
 * ENCAPSULAMENTO: todos os campos são privados.
 * Nenhuma classe externa pode alterar o estado diretamente — apenas via setters.
 */
public class Cliente implements Entidade {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private final LocalDate dataCadastro;

    public Cliente(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.dataCadastro = LocalDate.now();
    }

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public LocalDate getDataCadastro() { return dataCadastro; }

    @Override
    public String toString() {
        return String.format("Cliente[%d] %s | CPF: %s | %s | Cadastro: %s",
                id, nome, cpf, email, dataCadastro);
    }
}
