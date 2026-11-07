import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CampoJogo extends JPanel {
  public static final int LARGURA = 800; // Alinhado com o roteiro[cite: 11]
  public static final int ALTURA = 600;
  public static final int PONTOS_VITORIA = 10; // Meta de pontos para vencer a partida

  private Raquete raqueteplayer;
  private Raquete raquetecpu;
  private Bola bola;
  private Placar placar;

  private boolean subindo;
  private boolean descendo;
  private boolean partida_encerrada;

  private int tolerancia_cpu;
  private String nivel_dificuldade;
  private Timer loop;

  public CampoJogo() {
    this.setPreferredSize(new Dimension(LARGURA, ALTURA));
    this.setBackground(Color.BLACK);

    // Instanciando os objetos que compoem a partida (Composicao)
    this.raqueteplayer = new Raquete(20, (ALTURA - 80) / 2);
    this.raquetecpu = new Raquete(LARGURA - 30, (ALTURA - 80) / 2);
    this.bola = new Bola((LARGURA - 15) / 2, (ALTURA - 15) / 2);
    this.placar = new Placar();

    // Selecao de dificuldade no inicio do jogo (Facil, Medio, Dificil)
    selecionarDificuldade();
    this.bola.reiniciar(LARGURA, ALTURA, 1);

    // --- MAPEAMENTO DAS TECLAS (KEY BINDINGS) ---

    // 1. Tecla W e Seta Cima (Subir - Pressionar e Soltar)
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "pressionar_w");
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "pressionar_w");
    this.getActionMap().put("pressionar_w", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        subindo = true;
        System.out.println("Subindo: " + subindo);
      }
    });

    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "soltar_w");
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released UP"), "soltar_w");
    this.getActionMap().put("soltar_w", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        subindo = false;
        System.out.println("Subindo: " + subindo + " || y = " + raqueteplayer.getY());
      }
    });

    // 2. Tecla S e Seta Baixo (Descer - Pressionar e Soltar)
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "pressionar_s");
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "pressionar_s");
    this.getActionMap().put("pressionar_s", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        descendo = true;
        System.out.println("Descendo: " + descendo);
      }
    });

    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "soltar_s");
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released DOWN"), "soltar_s");
    this.getActionMap().put("soltar_s", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        descendo = false;
        System.out.println("Descendo: " + descendo + " || y = " + raqueteplayer.getY());
      }
    });

    // Limpa os comandos ativos caso a janela perca o foco para evitar movimento preso
    this.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(FocusEvent e) {
        subindo = false;
        descendo = false;
      }
    });

    // Ciclo de atualizacao do jogo (~60 FPS)
    this.loop = new Timer(16, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        atualizarJogo();
      }
    });
    this.loop.start();
  }

  // Exibe caixa de dialogo para selecionar a dificuldade no inicio do jogo
  private void selecionarDificuldade() {
    String[] opcoes = { "Fácil", "Médio", "Difícil" };
    int escolha = JOptionPane.showOptionDialog(
        this,
        "Selecione o nível de dificuldade para iniciar a partida:",
        "Pong Game - Dificuldade",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        opcoes,
        opcoes[1]);

    if (escolha == 0) {
      // Facil: CPU mais lenta e maior margem de tolerancia
      this.nivel_dificuldade = "Fácil";
      this.raquetecpu.setVelocidade(3);
      this.tolerancia_cpu = 25;
      this.bola.setVelocidadeBase(5);
    } else if (escolha == 2) {
      // Dificil: CPU rapida, menor tolerancia e bola mais veloz
      this.nivel_dificuldade = "Difícil";
      this.raquetecpu.setVelocidade(6);
      this.tolerancia_cpu = 8;
      this.bola.setVelocidadeBase(7);
    } else {
      // Medio (padrao caso feche o dialogo ou selecione Medio)
      this.nivel_dificuldade = "Médio";
      this.raquetecpu.setVelocidade(4);
      this.tolerancia_cpu = 16;
      this.bola.setVelocidadeBase(6);
    }

    System.out.println("Dificuldade selecionada: " + this.nivel_dificuldade);
  }

  // Metodo principal chamado pelo Timer a cada 16ms
  private void atualizarJogo() {
    if (this.partida_encerrada) {
      return;
    }

    // 1. Movimento da raquete do jogador (se pressionar ambas as teclas, permanece parada)
    if (this.subindo && !this.descendo) {
      this.raqueteplayer.moverCima();
    } else if (this.descendo && !this.subindo) {
      this.raqueteplayer.moverBaixo();
    }

    // 2. Comportamento da raquete do computador (CPU)
    atualizarCpu();

    // 3. Movimento da bola
    this.bola.mover();

    // 4. Colisao da bola com o teto e com o chao
    verificarColisaoParedes();

    // 5. Colisao da bola com as raquetes
    verificarColisaoRaquetes();

    // 6. Verificar pontuacao e condicao de vitoria
    verificarPontuacaoEVitoria();

    // 7. Solicita redesenho da tela
    repaint();
  }

  // Acompanha a posicao vertical da bola respeitando velocidade, limites e tolerancia
  private void atualizarCpu() {
    double diferenca_centros = this.bola.getCentroY() - this.raquetecpu.getCentroY();

    if (Math.abs(diferenca_centros) > this.tolerancia_cpu) {
      if (diferenca_centros < 0) {
        this.raquetecpu.moverCima();
      } else {
        this.raquetecpu.moverBaixo();
      }
    }
  }

  // Rebate a bola no teto (y <= 0) e no chao (y + diametro >= ALTURA)
  private void verificarColisaoParedes() {
    if (this.bola.getY() <= 0) {
      this.bola.setY(0);
      this.bola.inverterVelocidadeY();
    } else if (this.bola.getY() + this.bola.getDiametro() >= ALTURA) {
      this.bola.setY(ALTURA - this.bola.getDiametro());
      this.bola.inverterVelocidadeY();
    }
  }

  // Detecta intersecao entre os retangulos da bola e das raquetes e corrige sobreposicao
  private void verificarColisaoRaquetes() {
    Rectangle area_bola = this.bola.getAreaColisao();
    Rectangle area_player = this.raqueteplayer.getAreaColisao();
    Rectangle area_cpu = this.raquetecpu.getAreaColisao();

    // Rebote na raquete do jogador (esquerda): apenas quando a bola se move para a esquerda
    if (this.bola.getVelocidadeX() < 0 && area_bola.intersects(area_player)) {
      this.bola.setX(this.raqueteplayer.getX() + this.raqueteplayer.getLargura());
      this.bola.inverterVelocidadeX();
    }

    // Rebote na raquete da CPU (direita): apenas quando a bola se move para a direita
    if (this.bola.getVelocidadeX() > 0 && area_bola.intersects(area_cpu)) {
      this.bola.setX(this.raquetecpu.getX() - this.bola.getDiametro());
      this.bola.inverterVelocidadeX();
    }
  }

  // Verifica se a bola saiu completamente pelas laterais para marcar ponto
  private void verificarPontuacaoEVitoria() {
    // Saida pela esquerda: ponto para o computador
    if (this.bola.getX() + this.bola.getDiametro() <= 0) {
      this.placar.marcarPontoCpu();
      if (verificarFimDeJogo()) {
        return;
      }
      this.bola.reiniciar(LARGURA, ALTURA, 1);
      return;
    }

    // Saida pela direita: ponto para o jogador
    if (this.bola.getX() >= LARGURA) {
      this.placar.marcarPontoJogador();
      if (verificarFimDeJogo()) {
        return;
      }
      this.bola.reiniciar(LARGURA, ALTURA, -1);
      return;
    }
  }

  // Verifica se algum participante atingiu a meta de pontos
  private boolean verificarFimDeJogo() {
    String vencedor = null;

    if (this.placar.getPontosJogador() >= PONTOS_VITORIA) {
      vencedor = "Jogador";
    } else if (this.placar.getPontosCpu() >= PONTOS_VITORIA) {
      vencedor = "Computador (CPU)";
    }

    if (vencedor != null) {
      this.partida_encerrada = true;
      this.subindo = false;
      this.descendo = false;
      this.loop.stop(); // Para o timer antes de exibir a mensagem modal
      repaint();

      int resposta = JOptionPane.showConfirmDialog(
          this,
          "Fim de jogo! O vencedor foi: " + vencedor + "\nPlacar final: "
              + this.placar.getPontosJogador() + " x " + this.placar.getPontosCpu()
              + "\n\nDeseja jogar uma nova partida?",
          "Fim da Partida",
          JOptionPane.YES_NO_OPTION);

      if (resposta == JOptionPane.YES_OPTION) {
        reiniciarPartida();
      } else {
        System.exit(0);
      }
      return true;
    }

    return false;
  }

  // Restaura placar, posicoes, velocidades, comandos ativos e reinicia o mesmo timer
  private void reiniciarPartida() {
    this.subindo = false;
    this.descendo = false;

    selecionarDificuldade();

    this.placar.zerarPlacar();
    this.raqueteplayer.reiniciarPosicao(20, (ALTURA - this.raqueteplayer.getAltura()) / 2);
    this.raquetecpu.reiniciarPosicao(LARGURA - 30, (ALTURA - this.raquetecpu.getAltura()) / 2);
    this.bola.reiniciar(LARGURA, ALTURA, 1);

    this.partida_encerrada = false;
    this.loop.start();
  }

  @Override // Esse metodo override serve para avisar para o compilador que estou usando um método que já existe na classe mãe (JPanel)
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.WHITE);

    // Linha central
    g.drawLine(LARGURA / 2, 0, LARGURA / 2, ALTURA);

    // Desenho do Placar no alto do campo
    g.setFont(new Font("Monospaced", Font.BOLD, 36));
    g.drawString(String.valueOf(this.placar.getPontosJogador()), (LARGURA / 2) - 80, 50);
    g.drawString(String.valueOf(this.placar.getPontosCpu()), (LARGURA / 2) + 55, 50);

    // Indicador da dificuldade atual no topo
    g.setFont(new Font("SansSerif", Font.PLAIN, 14));
    String texto_dificuldade = "Dificuldade: " + this.nivel_dificuldade + " | Meta: " + PONTOS_VITORIA + " pts";
    FontMetrics fm = g.getFontMetrics();
    int x_texto = (LARGURA - fm.stringWidth(texto_dificuldade)) / 2;
    g.drawString(texto_dificuldade, x_texto, ALTURA - 15);

    // Raquete do Jogador
    g.fillRect(raqueteplayer.getX(), raqueteplayer.getY(), raqueteplayer.getLargura(), raqueteplayer.getAltura());

    // Raquete da CPU (corrigido para ler o Y da raquetecpu)
    g.fillRect(raquetecpu.getX(), raquetecpu.getY(), raquetecpu.getLargura(), raquetecpu.getAltura());

    // Bola em movimento
    g.fillOval(bola.getX(), bola.getY(), bola.getLargura(), bola.getAltura());
  }
}
