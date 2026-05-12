package com.ufv.locadora.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ENCAPSULAMENTO: todos os atributos são privados.
 * O acesso é feito exclusivamente via getters e setters gerados pelo Lombok.
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "data_cadastro", updatable = false)
    private LocalDate dataCadastro;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDate.now();
    }
}