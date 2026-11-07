import javax.swing.JFrame;

public class JanelaJogo extends JFrame {

  public JanelaJogo() {
    setTitle("Pong Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false); // Janela de tamanho fixo para simplificar os limites do campo

    CampoJogo painel = new CampoJogo();
    this.add(painel);

    this.pack();
    // this.setSize(800, 600); // Controle do tamanho da janela (substituido pelo pack com Dimension no CampoJogo)
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
