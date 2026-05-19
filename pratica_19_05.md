# Prática de Laboratório — Programação Orientada a Objetos
**Sistema de Controle de Fila de Atendimento — "ClinicaSim"**  
Duração: 60 minutos

---

> ⚠️ **Atenção antes de começar**  
> Esta prática contém detalhes propositalmente específicos e fora do padrão. Leia cada requisito com atenção antes de codificar. Respostas genéricas ou que ignorarem as restrições numeradas terão pontuação zerada nos critérios afetados.

---

## Contexto do problema

A clínica fictícia **ClinicaSim** possui um sistema manual de filas que precisa ser informatizado. Cada paciente recebe uma senha ao chegar e é chamado conforme a regra de prioridade da clínica — que não segue nenhum padrão publicado na internet. Você recebeu o caderno de anotações da recepcionista Dona Vilma e precisa modelá-lo em Java.

O sistema **não precisa** de interface gráfica, banco de dados ou arquivos. Apenas classes Java com lógica correta e um `main` demonstrativo.

---

## Cronograma sugerido

| Período | Atividade |
|---------|-----------|
| 0–10 min | Leitura e modelagem no papel |
| 10–30 min | Classes base + `Paciente` |
| 30–50 min | `FilaAtendimento` + regra de prioridade |
| 50–60 min | `main` + revisão |

---

## Requisitos de modelagem

### 1. Classe abstrata `Pessoa`

- Atributos: `nome` (String) e `cpf` (String, somente dígitos).
- Método abstrato `String identificacao()` que cada subclasse implementa de forma diferente.

---

### 2. Classe `Paciente` extends `Pessoa`

Adiciona os seguintes atributos:

- `int idadeAnos`
- `char tipoSanguineo` — aceita apenas os valores `'A'`, `'B'`, `'O'` ou `'X'`
- `boolean gestante`
- `int numeroSenha`

O método `identificacao()` deve retornar **exatamente** no formato:

```
[senha] NomeCompleto (tipo: X)
```

onde `X` é o valor de `tipoSanguineo`.

> **Armadilha #1:** `tipoSanguineo == 'X'` representa tipo desconhecido, **não** inválido. O sistema deve aceitá-lo normalmente.

---

### 3. Interface `Prioritario`

Define apenas o método:

```java
int calcularPeso();
```

Retorna um inteiro representando a prioridade do paciente na fila. **Quanto maior o peso, mais prioritário.**

---

### 4. Classe `PacienteFilaVilma` extends `Paciente` implements `Prioritario`

Implementa `calcularPeso()` conforme a **Regra da Dona Vilma** (ver seção abaixo). Esta é a classe central da prática.

---

### 5. Classe `FilaAtendimento`

Usa internamente um `ArrayList<PacienteFilaVilma>`. Métodos obrigatórios:

- `void entrar(PacienteFilaVilma p)` — adiciona ao final da lista.
- `PacienteFilaVilma chamarProximo()` — remove e retorna o paciente com **maior peso**. Em caso de empate de peso, retorna quem tem o **menor número de senha**.
- `int tamanho()` — retorna quantos pacientes aguardam.

> **Armadilha #2:** `chamarProximo()` em fila vazia deve lançar `IllegalStateException` com a mensagem `"Fila vazia"` — **não retornar null**.

---

## A Regra da Dona Vilma — `calcularPeso()`

> *"Eu anoto aqui como eu faço, tá? Começo com zero. Se a pessoa tiver mais de 60 anos, boto mais 10. Se for criança — menos de 12 anos — boto mais 7. Gestante sempre ganha mais 15. Agora, se o tipo sanguíneo for O, boto mais 5 por ser universal. E o 3º dígito do CPF — se ele for par (incluindo zero), boto mais 3. Mas se a pessoa for gestante E menor de 18 anos ao mesmo tempo, esse bônus de gestante não é 15, é 25. Os outros bônus continuam normais."*

| Condição | Bônus de peso | Observação |
|----------|:---:|-----------|
| Idade > 60 anos | +10 | — |
| Idade < 12 anos | +7 | — |
| Gestante (idade ≥ 18) | +15 | Regra normal |
| Gestante (idade < 18) | +25 | Substitui o +15 |
| Tipo sanguíneo == `'O'` | +5 | — |
| 3º dígito do CPF é par (0,2,4,6,8) | +3 | Índice 2 da string CPF |

> **Armadilha #3:** Os bônus são **aditivos** (exceto a substituição gestante/menor). Uma gestante menor de 18 com tipo O e 3º dígito par acumula: `25 + 5 + 3 = 33`.

---

## Método `main` obrigatório

Crie pelo menos **4 pacientes** que cubram os seguintes casos:

| Caso | Descrição |
|------|-----------|
| A | Paciente idoso (acima de 60), tipo `'O'`, CPF com 3º dígito par |
| B | Gestante menor de 18 anos |
| C | Criança (abaixo de 12), tipo sanguíneo `'X'` |
| D | Paciente comum sem nenhum bônus (verifique que o peso é **0**) |

Insira todos na fila e chame `chamarProximo()` quatro vezes, imprimindo `identificacao()` e o peso de cada um na **ordem de saída**.
