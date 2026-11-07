/*
  DUVIDAS:
  - [x] Entender Funcao anonima
  - [x] Ciclo de atualizacao com Timer e colisoes
*/

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    System.out.println("----- Iniciando PONG GAME ------------");

    SwingUtilities.invokeLater(() -> { // Funcao que ajuda no gerenciamento de memoria da aplicacao (thread de eventos do Swing)
      new JanelaJogo();
    });
  }
}
