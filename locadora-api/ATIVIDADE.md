# Atividade Prática — Orientação a Objetos em Java
## Sistema de Gerenciamento de Locadora de Veículos

**Disciplina:** Orientação a Objetos  
**Tipo:** Projeto Backend em Java  
**Nível:** Intermediário  
**Tempo estimado:** 8–12 horas  

---

## 1. Objetivo Pedagógico

Ao concluir esta atividade, você será capaz de:

- Aplicar os quatro pilares da orientação a objetos em um projeto real: **abstração**, **encapsulamento**, **herança** e **polimorfismo**
- Estruturar um sistema backend em Java seguindo a arquitetura em camadas (Controller → Service → Repository)
- Persistir dados com JPA/Hibernate em banco SQLite
- Documentar e testar uma API REST com Swagger/OpenAPI

---

## 2. Contextualização do Problema

A empresa **LocaFácil** gerencia uma frota de carros e motos para locação. O gerente precisa de um sistema para:

- Cadastrar e gerenciar os **veículos** da frota (carros e motos)
- Manter o cadastro de **clientes**
- Registrar e controlar as **locações** realizadas, com cálculo automático do valor com base no tipo do veículo

Você foi contratado como desenvolvedor backend para construir a API REST deste sistema.

---

## 3. Recursos Necessários

| Recurso | Versão mínima |
|---|---|
| Java JDK | 17 |
| Maven | 3.8+ |
| IDE | IntelliJ IDEA, Eclipse ou VS Code |
| Extensão REST Client | Insomnia, Postman ou curl |
| Navegador | Para acessar o Swagger UI |

**Dependências principais (já configuradas no `pom.xml`):**
- Spring Boot 3.2 (Web, Data JPA, Validation)
- SQLite JDBC + Hibernate Community Dialects
- Springdoc OpenAPI 2.3 (Swagger)
- Lombok

---

## 4. Modelo do Domínio

### Diagrama de Classes

```
            ┌─────────────────────────────┐
            │        <<abstract>>         │
            │           Veiculo           │
            │─────────────────────────────│
            │ - id: Long                  │
            │ - placa: String             │
            │ - marca: String             │
            │ - modelo: String            │
            │ - ano: int                  │
            │ - valorDiaria: BigDecimal   │
            │ - disponivel: boolean       │
            │─────────────────────────────│
            │ + calcularCustoLocacao(int) │  ← abstrato
            │ + getTipoVeiculo(): String  │  ← abstrato
            └──────────┬──────────────────┘
                       │ herança
          ┌────────────┴────────────┐
          │                         │
┌─────────▼──────────┐   ┌──────────▼──────────┐
│        Carro        │   │         Moto         │
│────────────────────│   │─────────────────────│
│ - numeroPortas: int │   │ - cilindradas: int   │
│ - tipoCombustivel   │   │─────────────────────│
│────────────────────│   │ calcularCusto()      │
│ calcularCusto()    │   │  = diária×dias×0.9  │
│  = diária × dias   │   └──────────────────────┘
└────────────────────┘

┌───────────────────────┐       ┌──────────────────────────┐
│        Cliente        │       │         Locação           │
│───────────────────────│  N  1 │──────────────────────────│
│ - id: Long            ├───────┤ - cliente: Cliente        │
│ - nome: String        │       │ - veiculo: Veiculo        │
│ - cpf: String         │  N  1 │ - dataInicio: LocalDate   │
│ - email: String       ├───────┤ - dataFim: LocalDate      │
│ - telefone: String    │       │ - valorTotal: BigDecimal  │
│ - dataNascimento      │       │ - status: StatusLocacao   │
└───────────────────────┘       └──────────────────────────┘

StatusLocacao (enum): ATIVA | FINALIZADA | CANCELADA
```

### Regra de Negócio de Cálculo

| Tipo | Fórmula |
|---|---|
| Carro | `valorDiaria × numeroDias` |
| Moto | `valorDiaria × numeroDias × 0,90` (desconto de 10%) |

---

## 5. Passo a Passo

### Passo 1 — Configurar o projeto

1. Importe o projeto na sua IDE como um projeto Maven
2. Aguarde o download das dependências
3. Execute a aplicação com `mvn spring-boot:run` ou pela IDE
4. Acesse o Swagger UI em: `http://localhost:8080/swagger-ui.html`

### Passo 2 — Explorar a estrutura do projeto

Localize e leia os arquivos abaixo, identificando onde cada conceito de OO aparece:

```
src/main/java/com/ufv/locadora/
├── model/
│   ├── Veiculo.java          ← ABSTRAÇÃO + base da HERANÇA
│   ├── Carro.java            ← HERANÇA + POLIMORFISMO
│   ├── Moto.java             ← HERANÇA + POLIMORFISMO
│   ├── Cliente.java          ← ENCAPSULAMENTO
│   ├── Locacao.java          ← COMPOSIÇÃO
│   └── StatusLocacao.java    ← ENUM (tipo especial de classe)
├── repository/               ← acesso ao banco de dados
├── service/                  ← regras de negócio
│   └── VeiculoService.java   ← Factory Method + uso do polimorfismo
├── controller/               ← endpoints REST
└── dto/                      ← objetos de transferência de dados
```

### Passo 3 — Testar os CRUDs via Swagger

Acesse `http://localhost:8080/swagger-ui.html` e execute os cenários na ordem:

#### Cenário A — Cadastro de veículos

**Criar um carro:**
```json
POST /api/veiculos
{
  "tipo": "CARRO",
  "placa": "ABC1D23",
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2022,
  "valorDiaria": 150.00,
  "numeroPortas": 4,
  "tipoCombustivel": "FLEX"
}
```

**Criar uma moto:**
```json
POST /api/veiculos
{
  "tipo": "MOTO",
  "placa": "XYZ5E67",
  "marca": "Honda",
  "modelo": "CB 500",
  "ano": 2023,
  "valorDiaria": 80.00,
  "cilindradas": 500
}
```

#### Cenário B — Cadastro de cliente

```json
POST /api/clientes
{
  "nome": "Maria Oliveira",
  "cpf": "12345678901",
  "email": "maria@email.com",
  "telefone": "31987654321",
  "dataNascimento": "1995-06-15"
}
```

#### Cenário C — Realizar locação

```json
POST /api/locacoes
{
  "clienteId": 1,
  "veiculoId": 1,
  "dataInicio": "2026-05-20",
  "dataFim": "2026-05-25"
}
```

Observe o `valorTotal` retornado: deve ser `150.00 × 5 dias = 750.00` (carro).

Repita com a moto e compare: `80.00 × 5 × 0.90 = 360.00`.

**Esta diferença é o polimorfismo funcionando!**

#### Cenário D — Finalizar locação

```
PATCH /api/locacoes/1/finalizar
```

Tente alugar o mesmo veículo novamente — o sistema deve retornar erro de indisponibilidade.

### Passo 4 — Analisar os conceitos no código

Responda por escrito (Questão Analítica):

1. Abra `Veiculo.java`. Por que a classe e o método `calcularCustoLocacao` são declarados como `abstract`? O que aconteceria se tentasse criar `new Veiculo()`?

2. Em `VeiculoService.java`, o método `fabricarVeiculo()` usa `switch` para criar `Carro` ou `Moto`. Esse padrão chama-se **Factory Method**. O que ele tem a ver com o princípio da abstração?

3. Em `LocacaoService.java`, linha com `veiculo.calcularCustoLocacao(numeroDias)` — nesse ponto o sistema não sabe se é um carro ou moto. Como Java decide qual implementação executar? Esse mecanismo chama-se...?

4. Compare `Cliente.java` com uma classe que tivesse todos os atributos `public`. O que muda em termos de segurança e controle do estado interno?

5. O `StatusLocacao` é um `enum`. Qual vantagem isso tem sobre usar uma `String` ou `int` para representar o status?

### Passo 5 — Extensão obrigatória

Implemente uma nova funcionalidade **sem quebrar o código existente**:

**Adicione o endpoint:**
```
GET /api/locacoes/ativas
```
Que retorna apenas as locações com `status = ATIVA`.

Para isso, você precisará:
1. Adicionar método no `LocacaoRepository`
2. Adicionar método no `LocacaoService`
3. Adicionar endpoint no `LocacaoController`

---

## 6. Entregáveis Esperados

| Item | Descrição |
|---|---|
| Código-fonte | Projeto completo funcionando, com a extensão do Passo 5 implementada |
| Relatório (PDF) | Respostas às 5 questões analíticas do Passo 4 (mínimo 1 parágrafo cada) |
| Screenshot | Imagem do Swagger UI com pelo menos 3 endpoints executados com sucesso |

---

## 7. Tempo Estimado

| Fase | Tempo |
|---|---|
| Configuração e execução do projeto | 30 min |
| Exploração do código e testes via Swagger | 1–2 horas |
| Resposta às questões analíticas | 1–2 horas |
| Implementação da extensão (Passo 5) | 1–2 horas |
| Revisão e entrega | 30 min |
| **Total** | **4–7 horas** |

---

## 8. Extensão Opcional (Desafio para Alunos Avançados)

Escolha **uma** das extensões abaixo:

### Opção A — Nova subclasse
Crie a classe `Caminhonete` que estende `Veiculo` com os atributos `capacidadeCargaKg` e `tracao` (4x4 ou 4x2). Implemente a regra: caminhonetes cobram 20% a mais que o valor base da diária.

### Opção B — Relatório financeiro
Crie o endpoint `GET /api/locacoes/relatorio` que retorna:
- Total de locações finalizadas
- Receita total gerada (soma dos `valorTotal`)
- Valor médio por locação
- Tipo de veículo mais alugado

### Opção C — Validação de CNH
Adicione o campo `numeroCnh` ao `Cliente` e crie uma validação de negócio: ao criar uma locação, verificar se o cliente possui CNH cadastrada. Se não tiver, retornar erro 422 com mensagem clara.

---

## Referências

- DEITEL, P.; DEITEL, H. *Java: Como Programar*. 10. ed. Pearson, 2017. Cap. 9 e 10 (Herança e Polimorfismo).
- GAMMA, E. et al. *Padrões de Projeto: Soluções Reutilizáveis*. Bookman, 2000. (Factory Method)
- Documentação Spring Boot: https://spring.io/projects/spring-boot
- Documentação Springdoc OpenAPI: https://springdoc.org
