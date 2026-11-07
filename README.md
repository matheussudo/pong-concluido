# Pong em Java - Programacao Orientada a Objetos

Implementacao completa do classico jogo Pong em Java, desenvolvida como projeto pratico de Programacao Orientada a Objetos (POO) utilizando exclusivamente a biblioteca padrao do Java SE (`javax.swing` e `java.awt`).

---

## Sobre o Projeto

O objetivo deste projeto e aplicar na pratica os pilares da Orientacao a Objetos em uma aplicacao grafica interativa em tempo real. Toda a estrutura foi dividida em classes com responsabilidades bem definidas, evitando acoplamento desnecessario e mantendo o estado da partida protegido por encapsulamento.

Principais conceitos aplicados:

- **Heranca (`extends`):** Utilizada de forma semantica onde existe a relacao "e um" (`JanelaJogo extends JFrame` e `CampoJogo extends JPanel`).
- **Composicao ("tem um"):** O painel principal (`CampoJogo`) instancia, coordena e gerencia o ciclo de vida dos objetos que compoem a partida: duas instancias de `Raquete`, uma `Bola` e um `Placar`.
- **Encapsulamento:** Todos os atributos de posicao, velocidade e pontuacao sao privados (`private`), sendo lidos ou alterados apenas por metodos especificos que garantem as regras de negocio e os limites do campo.
- **Ciclo de Jogo e Eventos:** Atualizacao continua de quadro (~60 FPS) por meio de `javax.swing.Timer` combinada ao mapeamento de teclas via `KeyBindings` (`InputMap` e `ActionMap`), garantindo movimentacao fluida sem depender da taxa de repeticao do teclado do sistema operacional.

---

## Arquitetura do Sistema

| Classe | Responsabilidade Principal |
| :--- | :--- |
| `Main` | Ponto de entrada da aplicacao. Inicializa a interface grafica na Event Dispatch Thread (`SwingUtilities.invokeLater`). |
| `JanelaJogo` | Configura a janela principal (`JFrame`), define tamanho fixo (`setResizable(false)`) e adiciona o painel do jogo. |
| `CampoJogo` | Coordena o estado da partida, captura os eventos de teclado, executa o loop principal (`Timer`), trata colisoes e renderiza os elementos em `paintComponent`. |
| `Raquete` | Modela o estado e o comportamento das raquetes (jogador e CPU), encapsulando posicao, dimensoes, velocidade, limites verticais e area de colisao. |
| `Bola` | Controla coordenadas, deslocamento horizontal/vertical, progressao gradual de velocidade a cada rebote, inversao de direcao e reposicionamento centralizado apos cada ponto. |
| `Placar` | Gerencia a pontuacao do jogador e do computador de forma isolada, fornecendo metodos para contabilizar pontos e reiniciar a contagem. |
| `TesteJogo` | Suite de testes automatizados que verifica movimentacao, limites do campo, colisoes, encapsulamento do placar e progressao de velocidade por dificuldade. |

---

## Funcionalidades e Mecanicas

### 1. Selecao de Dificuldade e Aumento Gradual de Velocidade
Ao iniciar o jogo (ou ao comecar uma nova partida), uma caixa de dialogo permite selecionar entre tres niveis de dificuldade. Cada nivel define tanto a precisao/velocidade da inteligencia artificial (CPU) quanto a **velocidade com que a bola acelera durante a troca de rebotes**:

| Nivel | Velocidade da CPU | Tolerancia da CPU | Velocidade Inicial da Bola | Aceleracao da Bola | Velocidade Maxima |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Facil** | 3 px/ciclo | 25 px | 4 px/ciclo | Aumenta a cada **6 rebotes** | 10 px/ciclo |
| **Medio** | 4 px/ciclo | 16 px | 5 px/ciclo | Aumenta a cada **3 rebotes** | 12 px/ciclo |
| **Dificil** | 6 px/ciclo | 8 px | 6 px/ciclo | Aumenta a cada **1 rebote** | 15 px/ciclo |

Quando um ponto e marcado, a bola retorna ao centro do campo com a velocidade inicial de saque do nivel selecionado.

### 2. Inteligencia Artificial da CPU
A raquete do computador acompanha a bola comparando o centro vertical da bola com o seu proprio centro vertical. Para evitar oscilacoes visuais (tremores quando a bola esta alinhada), foi implementada uma faixa de tolerancia em pixels dentro da qual a raquete permanece estavel.

### 3. Sistema de Colisoes
As colisoes entre a bola e as raquetes utilizam intersecao de retangulos (`java.awt.Rectangle.intersects`). Para evitar que a bola fique presa dentro da raquete ou sofra multiplos rebotes em um unico contato:
- Verifica-se o sinal da velocidade horizontal antes de inverter a direcao;
- A bola e reposicionada imediatamente na borda externa da raquete apos o impacto;
- O rebote e contabilizado pelo metodo `registrarReboteEAcelerar()` para aplicar a progressao de velocidade correspondente ao nivel de dificuldade.

### 4. Pontuacao, Condicao de Vitoria e Reinicio
- Sempre que a bola ultrapassa completamente a borda esquerda, o computador marca 1 ponto; ao ultrapassar a borda direita, o jogador marca 1 ponto.
- Apos cada ponto, a bola retorna ao centro do campo, restaura a velocidade base de saque e sai na direcao oposta.
- A partida encerra quando um dos participantes atinge **10 pontos**. O `Timer` e interrompido, o vencedor e exibido na tela e o jogador pode iniciar uma nova partida sem duplicar timers em memoria.

---

## Controles

| Tecla | Acao |
| :--- | :--- |
| `W` ou `Seta para Cima` | Move a raquete do jogador para cima |
| `S` ou `Seta para Baixo` | Move a raquete do jogador para baixo |

> Nota: Caso ambas as teclas sejam pressionadas simultaneamente, os comandos se anulam e a raquete permanece parada. Se a janela perder o foco, os comandos ativos sao limpos automaticamente.

---

## Como Compilar e Executar

### Pre-requisitos
- **Java Development Kit (JDK):** Versao 8 ou superior instalado e configurado no `PATH`.
- Ambiente grafico (Desktop) ativo.

### Passo a passo no terminal

1. Clone o repositorio e acesse a pasta do projeto:
```bash
git clone https://github.com/matheussudo/pong-concluido.git
cd pong-concluido
```

2. Compile todos os arquivos `.java`:
```bash
javac -encoding UTF-8 *.java
```

3. Execute a classe principal:
```bash
java Main
```

---

## Testes Automatizados e Registro de Verificacao

O projeto conta com a classe `TesteJogo.java` e o script `executar_testes.sh` para validar de forma automatizada as regras de negocio exigidas nas fases do roteiro (limites das raquetes, centralizacao e inversao da bola, intersecao de retangulos de colisao, encapsulamento do placar e progressao de velocidade nos niveis Facil, Medio e Dificil).

Para executar a suite de testes no terminal:

```bash
chmod +x executar_testes.sh
./executar_testes.sh
```

Ou diretamente com o Java:

```bash
javac -encoding UTF-8 *.java
java TesteJogo
```

---

## Estrutura de Arquivos

```text
.
├── Main.java              # Inicializacao da aplicacao na thread de eventos do Swing
├── JanelaJogo.java        # Configuracao do JFrame e empacotamento do painel
├── CampoJogo.java         # Loop principal, eventos de teclado, IA, colisoes e pintura
├── Raquete.java           # Modelagem das raquetes do jogador e do computador
├── Bola.java              # Modelagem da bola, movimentacao, progressao de velocidade e saque
├── Placar.java            # Controle encapsulado da pontuacao da partida
├── TesteJogo.java         # Suite de testes unitarios/funcionais das classes do jogo
├── executar_testes.sh     # Script automatizado para compilar e rodar os testes
└── README.md              # Documentacao do projeto
```
