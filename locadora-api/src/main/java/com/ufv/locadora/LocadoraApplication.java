package com.ufv.locadora;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Locadora de Veículos API",
        version = "1.0",
        description = "Sistema de gerenciamento de locadora de veículos. " +
                      "Prática de Orientação a Objetos — demonstra abstração, " +
                      "herança, polimorfismo e encapsulamento.",
        contact = @Contact(name = "Disciplina de Orientação a Objetos")
    )
)
public class LocadoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocadoraApplication.class, args);
    }
}