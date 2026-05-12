# Atividade Prática — Orientação a Objetos em Java
## Sistema de Locadora de Veículos — Java Puro com Armazenamento em Memória

**Disciplina:** Orientação a Objetos  
**Tipo:** Projeto Java sem frameworks  
**Nível:** Introdutório–Intermediário  
**Tempo estimado:** 5–8 horas  

---

## 1. Objetivo Pedagógico

Ao concluir esta atividade, você será capaz de:

- Identificar e aplicar os quatro pilares da OO — **abstração**, **encapsulamento**, **herança** e **polimorfismo** — em código Java puro, sem frameworks
- Compreender como **interfaces** e **classes abstratas** definem contratos e moldes para outras classes
- Entender o uso de **generics** (`<T>`) para criar componentes reutilizáveis
- Praticar **injeção de dependência manual** — padrão que independe de frameworks
- Estender um sistema existente adicionando novas funcionalidades com coesão

> **Por que Java puro?**  
> Nesta atividade não há Spring Boot, Hibernate nem Lombok.  
> Tudo que acontece está visível no código. Isso permite que você leia cada linha e entenda *por que* ela existe — sem "mágica" de framework.

---

## 2. Contextualização do Problema

A empresa **LocaFácil** precisa de um sistema para controlar sua frota de carros e motos. Como os dados não precisam sobreviver ao encerramento do programa nesta fase, o armazenamento é feito **em memória**, usando as coleções nativas do Java (`Map`, `List`).

O sistema já foi construído. Sua missão é **entendê-lo, testá-lo e expandi-lo**.

---

## 3. Recursos Necessários

| Recurso | Versão mínima |
|---|---|
| Java JDK | 17 |
| Maven | 3.8+ (opcional — pode compilar com `javac` também) |
| IDE | IntelliJ IDEA, Eclipse ou VS Code |

**Nenhuma dependência externa.** O `pom.xml` contém apenas configurações do compilador.  
Para executar sem Maven: `javac` + `java` são suficientes.

---

## 4. Modelo do Domínio

### Diagrama de Classes

```
         «interface»                   «interface»
          Entidade                     Repositorio<T>
         ──────────                   ────────────────
         + getId()                    + salvar(T)
         + setId(Long)                + buscarPorId(Long)
              ▲                       + listarTodos()
              │ implementa            + deletar(Long)
              │                       + contarTotal()
              │                              ▲
     ┌────────┴───────────────────┐          │ implementa (com T extends Entidade)
     │      «abstract»            │   ┌──────┴──────────────────┐
     │    RepositorioEmMemoria<T> │   │  «abstract class»        │
     │    ────────────────────── │   │  RepositorioEmMemoria<T>  │
     │    # dados: Map<Long, T>  │   └──────────────────────────┘
     └────────────────────────────┘          ▲ herança
                                             │
              ┌──────────────────────────────┤
              │                              │
   VeiculoRepositorioEmMemoria   ClienteRepositorioEmMemoria  ...


          «abstract»
           Veiculo  implements Entidade
          ──────────────────────────────
          - id: Long
          - placa, marca, modelo: String
          - ano: int
          - valorDiaria: double
          - disponivel: boolean
          ──────────────────────────────
          + calcularCustoLocacao(int)  «abstract»
          + getTipoVeiculo(): String   «abstract»
               ▲ herança
       ┌────────┴────────┐
       │                 │
     Carro             Moto
   ─────────        ─────────
   - numeroPortas   - cilindradas
   - tipoCombust.
   ─────────        ─────────
   calcular()       calcular()
   = d × dias       = d × dias × 0,9


   Cliente  implements Entidade      Locacao  implements Entidade
   ────────────────────────────      ──────────────────────────────
   - id, nome, cpf, email            - id
   - telefone, dataNascimento        - cliente: Cliente      ←┐ composição
   - dataCadastro (final)            - veiculo: Veiculo      ←┘
                                     - dataInicio, dataFim
                                     - valorTotal (final)
                                     - status: StatusLocacao

StatusLocacao (enum): ATIVA | FINALIZADA | CANCELADA
```

### Hierarquia de Repositórios

```
Repositorio<T>  «interface»
       ▲
       │ implementa
RepositorioEmMemoria<T extends Entidade>  «abstract class»
       ▲ herança
       ├── VeiculoRepositorioEmMemoria  implements VeiculoRepositorio
       ├── ClienteRepositorioEmMemoria  implements ClienteRepositorio
       └── LocacaoRepositorioEmMemoria  implements LocacaoRepositorio
```

### Regras de Negócio

| Tipo | Cálculo de Custo |
|---|---|
| `Carro` | `valorDiaria × numeroDias` |
| `Moto` | `valorDiaria × numeroDias × 0,90` (10% de desconto) |

---

## 5. Passo a Passo

### Passo 1 — Executar o projeto

**Com Maven:**
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.ufv.locadora.Main"
```

**Sem Maven (apenas javac):**
```bash
# Na raiz do projeto:
find src -name "*.java" > fontes.txt
javac -d out @fontes.txt
java -cp out com.ufv.locadora.Main
```

Observe a saída no terminal. Ela está organizada em **7 seções**, cada uma demonstrando um conceito diferente de OO.

---

### Passo 2 — Explorar a estrutura de pacotes

Abra cada arquivo na ordem abaixo e leia o código antes de responder às questões:

```
src/main/java/com/ufv/locadora/
│
├── model/
│   ├── Entidade.java              ← interface: o menor contrato possível
│   ├── Veiculo.java               ← classe abstrata com método abstrato
│   ├── Carro.java                 ← subclasse concreta
│   ├── Moto.java                  ← subclasse concreta
│   ├── Cliente.java               ← encapsulamento explícito (getters/setters)
│   ├── Locacao.java               ← composição e polimorfismo no construtor
│   └── StatusLocacao.java         ← enum
│
├── repositorio/
│   ├── Repositorio.java           ← interface genérica <T>
│   ├── RepositorioEmMemoria.java  ← classe abstrata genérica <T extends Entidade>
│   ├── VeiculoRepositorio.java    ← interface que herda de Repositorio<Veiculo>
│   ├── VeiculoRepositorioEmMemoria.java  ← implementação concreta
│   └── (idem para Cliente e Locacao)
│
├── servico/
│   ├── VeiculoServico.java        ← regras de negócio + injeção via construtor
│   ├── ClienteServico.java
│   └── LocacaoServico.java
│
├── excecao/
│   ├── NegocioException.java      ← RuntimeException para regras violadas
│   └── EntidadeNaoEncontradaException.java
│
└── Main.java                      ← ponto de entrada e demonstração
```

---

### Passo 3 — Questões analíticas

Responda por escrito, citando o arquivo e a linha relevante do código:

**Q1 — Abstração e Herança**  
Abra `Veiculo.java`. A classe é declarada como `abstract` e possui o método `calcularCustoLocacao` também `abstract`.

- a) O que acontece se você tentar escrever `new Veiculo(...)` em qualquer parte do código? Por quê?
- b) O construtor de `Veiculo` é `protected` (não `public`). Por que isso faz sentido para uma classe abstrata?
- c) Em `Carro.java`, a primeira linha do construtor é `super(placa, marca, ...)`. O que `super()` faz aqui? O que aconteceria se você removesse essa chamada?

**Q2 — Encapsulamento**  
Abra `Cliente.java` e observe o campo `dataCadastro`:

```java
private final LocalDate dataCadastro;
```

- a) Por que `dataCadastro` é `final`? O que isso impede?
- b) Não existe `setDataCadastro()`. Como a data é definida então? (Dica: veja o construtor)
- c) Se todos os campos de `Cliente` fossem `public`, que tipo de problema poderia ocorrer num sistema real?

**Q3 — Polimorfismo**  
Abra `Locacao.java` e encontre a linha:

```java
this.valorTotal = veiculo.calcularCustoLocacao(dias);
```

- a) Nesse momento, `veiculo` é do tipo `Veiculo` (referência da classe pai). Como Java sabe se deve chamar o método de `Carro` ou de `Moto`?
- b) Esse mecanismo chama-se **ligação dinâmica** (*dynamic dispatch*). Qual é a vantagem disso em relação a um `if (tipo.equals("CARRO"))` manual?
- c) Se uma nova classe `Caminhonete extends Veiculo` fosse criada, a classe `Locacao` precisaria ser modificada? Por quê?

**Q4 — Interface e Classe Abstrata**  
Compare `Repositorio.java` (interface) com `RepositorioEmMemoria.java` (classe abstrata).

- a) Qual é a diferença entre uma interface e uma classe abstrata em Java?
- b) Por que `Repositorio<T>` foi criada como interface, e não diretamente como a classe abstrata?
- c) A declaração `<T extends Entidade>` em `RepositorioEmMemoria` significa que `T` deve implementar `Entidade`. Por que isso é necessário para que `entidade.setId(proximoId++)` funcione?

**Q5 — Injeção de Dependência**  
Abra `VeiculoServico.java` e observe o construtor:

```java
public VeiculoServico(VeiculoRepositorio repositorio) {
    this.repositorio = repositorio;
}
```

E em `Main.java`:

```java
var veiculoRepositorio = new VeiculoRepositorioEmMemoria();
var veiculoServico = new VeiculoServico(veiculoRepositorio);
```

- a) Por que `VeiculoServico` recebe o repositório como parâmetro em vez de criar `new VeiculoRepositorioEmMemoria()` internamente?
- b) Se amanhã você precisasse salvar os dados em arquivo (não mais em memória), quais classes precisariam mudar? Quais **não** precisariam?
- c) Esse padrão é chamado de **Inversão de Dependência**. Como ele se relaciona com o princípio de depender de abstrações (`VeiculoRepositorio`) e não de implementações (`VeiculoRepositorioEmMemoria`)?

---

### Passo 4 — Extensão obrigatória

Implemente **as duas tarefas abaixo** sem alterar as classes existentes (apenas adicione novas ou estenda):

#### Tarefa A — Nova subclasse

Crie a classe `Caminhonete` que estende `Veiculo` com:
- Atributo `int capacidadeCargaKg`
- Regra: caminhonetes cobram 25% a mais que o valor base (`valorDiaria × dias × 1.25`)
- `getTipoVeiculo()` retorna `"CAMINHONETE"`

Adicione uma caminhonete no `Main` e verifique que o cálculo aparece correto na saída.

#### Tarefa B — Relatório em console

Crie a classe `RelatorioServico` com o método:

```java
public void imprimirResumo(VeiculoServico vs, ClienteServico cs, LocacaoServico ls)
```

Que imprime no console:

```
===== RELATÓRIO GERENCIAL =====
Total de veículos: 3
  Disponíveis: 1 | Em locação: 2
Total de clientes: 2
Total de locações: 3
  Ativas: 1 | Finalizadas: 1 | Canceladas: 1
Receita total (finalizadas): R$ 750,00
Receita média por locação: R$ 250,00
```

---

## 6. Entregáveis Esperados

| Item | Descrição |
|---|---|
| Código-fonte | Projeto completo com as Tarefas A e B implementadas |
| Relatório (PDF ou .md) | Respostas às 5 questões analíticas (mínimo 1 parágrafo cada) |
| Saída do terminal | Captura ou arquivo `.txt` da saída completa do `Main` após suas alterações |

---

## 7. Tempo Estimado

| Fase | Tempo |
|---|---|
| Executar e observar a saída do `Main` | 20 min |
| Leitura da estrutura de pacotes (Passo 2) | 1–2 horas |
| Respostas às questões analíticas | 1–2 horas |
| Implementação da Tarefa A (Caminhonete) | 30–60 min |
| Implementação da Tarefa B (Relatório) | 1–2 horas |
| Revisão e entrega | 30 min |
| **Total** | **4–8 horas** |

---

## 8. Extensão Opcional — Desafios para Alunos Avançados

Escolha **uma** das opções abaixo:

### Opção A — Menu interativo com Scanner

Substitua o `Main` estático por um menu de console interativo que aceite entrada do usuário:

```
====== MENU LOCADORA ======
1. Cadastrar Carro
2. Cadastrar Moto
3. Listar Veículos Disponíveis
4. Cadastrar Cliente
5. Realizar Locação
6. Finalizar Locação
7. Relatório
0. Sair
Escolha: _
```

Use `Scanner` para ler a opção e estruture o loop com `while` e `switch`.

### Opção B — Ordenação com Comparable

Faça `Cliente` implementar `Comparable<Cliente>` para ordenação alfabética por nome:

```java
public class Cliente implements Entidade, Comparable<Cliente> {
    @Override
    public int compareTo(Cliente outro) {
        return this.nome.compareTo(outro.nome);
    }
}
```

Em seguida, modifique `ClienteRepositorioEmMemoria.listarTodos()` para retornar a lista **ordenada**.  
Pesquise a diferença entre `Comparable` e `Comparator` e explique quando usar cada um.

### Opção C — Persistência em arquivo de texto

Crie `VeiculoRepositorioArquivo` que implementa `VeiculoRepositorio` salvando os dados num arquivo `.csv`. Troque a implementação no `Main`:

```java
var veiculoRepositorio = new VeiculoRepositorioArquivo("veiculos.csv");
```

O restante do sistema **não deve precisar de nenhuma alteração** — apenas essa linha muda.  
Isso demonstra o poder da abstração: o serviço não sabe (nem precisa saber) onde os dados estão guardados.

---

## Comparativo: Este Projeto vs. Versão com Spring Boot

| Aspecto | Este projeto (Java puro) | `locadora-api` (Spring Boot) |
|---|---|---|
| Dependências | Nenhuma | Spring, JPA, SQLite, Lombok, Swagger |
| Getters/Setters | Escritos manualmente | Gerados pelo Lombok (`@Getter @Setter`) |
| Repositório | `HashMap` em memória | Banco SQLite via Hibernate |
| Injeção de dependência | Manual no `Main` | Automática pelo Spring (`@Autowired`) |
| API HTTP | Não possui | REST com Swagger em `localhost:8080` |
| Foco | Conceitos puros de OO | OO aplicada com boas práticas de mercado |

Ambos os projetos usam **o mesmo domínio** e **os mesmos padrões de design** — a diferença está na infraestrutura ao redor, não na OO em si.

---

## Referências

- DEITEL, P.; DEITEL, H. *Java: Como Programar*. 10. ed. Pearson, 2017.  
  Cap. 9 (Herança), Cap. 10 (Polimorfismo), Cap. 11 (Interfaces)
- BLOCH, J. *Effective Java*. 3. ed. Addison-Wesley, 2018.  
  Item 15 (Minimize mutabilidade), Item 17 (Prefira interfaces)
- Documentação oficial Java: https://docs.oracle.com/en/java/javase/17/
