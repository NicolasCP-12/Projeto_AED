# Projeto AED – Sistema de Gestão de Encomendas e Frota (Fase 1)

Trabalho prático de Algoritmos e Estruturas de Dados, Fase 1.

## Requisitos

Java 17 ou superior (JDK). Não usa bibliotecas externas.

## Compilar

Na pasta do projeto (a que contém `src`):

Linux / macOS:
```
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

Windows (PowerShell):
```
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Path src -Filter *.java).FullName
```

## Executar

| Comando | O que faz |
|---|---|
| `java -cp out Main` | Usa os ficheiros de dados da pasta atual (`encomendas.txt`, `veiculos.txt`, `intervalos.txt`) |
| `java -cp out Main exemplos_com_erros` | Usa ficheiros com linhas inválidas, para demonstrar a validação e o tratamento de erros |
| `java -cp out Main <pasta>` | Usa os ficheiros de outra pasta; se não existirem, são criados com dados de exemplo |

No IntelliJ IDEA: abrir a pasta do projeto e executar a classe `Main` (a pasta de trabalho deve ser a raiz do projeto).
No Windows, se os acentos aparecerem mal na consola, executar antes `chcp 65001`.

## Menu

| Opção | Funcionalidade | Estrutura / algoritmo |
|---|---|---|
| 1 | Registar encomenda (prioridade calculada) | Heap binário (Max-Heap) |
| 2 | Consultar a encomenda mais prioritária | Heap binário |
| 3 | Listar pendentes por prioridade | Heap binário |
| 4 | Alterar prioridade | Heap binário |
| 5 | Cancelar encomenda | Heap binário |
| 6 / 7 | Adicionar / listar veículos | Lista de veículos |
| 8 | Carregar veículo | Algoritmo guloso (Empacotador) |
| 9 | Planear entregas | MergeSort + seleção de intervalos |
| 10 | Adicionar janela de entrega | |
| 11 | Navegar no histórico (anterior, seguinte, início, fim) | Lista duplamente ligada |
| 12 / 13 | Desfazer / refazer | Lista duplamente ligada + padrão Command |
| 0 | Sair, com opção de guardar os dados | Ficheiros de texto |

## Cálculo da prioridade

```
P = 10 × urgência + taxa paga / 5 + horas de espera (máximo 48)
```

A urgência (1 a 5) é o critério dominante, a taxa paga distingue encomendas igualmente urgentes
e o tempo de espera impede que encomendas pouco urgentes fiquem indefinidamente na fila.

## Ficheiros de exemplo

Campos separados por `;`, linhas iniciadas por `#` são comentários, datas no formato `2026-10-26T09:00`.

| Ficheiro | Formato |
|---|---|
| `veiculos.txt` | `id;pesoMaximo;volumeMaximo` |
| `encomendas.txt` | `id;peso;volume;tempoChegada;prioridade;veiculo` (veículo vazio = encomenda pendente) |
| `intervalos.txt` | `inicio;fim` |

- Raiz do projeto: 6 encomendas, 2 veículos e 6 janelas de entrega válidos.
- `exemplos_com_erros/`: identificadores repetidos, peso e capacidade inválidos, datas e números mal formados, veículo inexistente e janela com fim antes do início. Cada linha inválida é ignorada e o motivo é indicado, sem interromper a leitura.

## Teste sugerido

1. `java -cp out Main` e opção 3: as encomendas aparecem por ordem decrescente de prioridade.
2. Opção 8 com `V01`: são carregadas as encomendas mais prioritárias que cabem em 60 kg e 2,5 m3.
3. Opção 12 e depois 7: a última atribuição é desfeita; opção 13 volta a aplicá-la.
4. Opção 11: percorrer o histórico com `A`, `S`, `I` e `F`.
5. Opção 9: são selecionadas 4 das 6 janelas, sem sobreposição.
6. Opção 0 e guardar com `S`; ao executar de novo, as encomendas atribuídas continuam nos veículos.
