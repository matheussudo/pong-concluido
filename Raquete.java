import java.awt.Rectangle;

public class Raquete {
  private int loc_x, loc_y, altura_raquete, largura_raquete;
  private int velocidade_raquete;

  public Raquete(int xInicial, int yInicial) { // Definindo a estrutura do objeto Raquete
    this.loc_x = xInicial;
    this.loc_y = yInicial;

    this.altura_raquete = 80;
    this.largura_raquete = 10;

    this.velocidade_raquete = 6;
  }

  // Criei esses métodos publicos para "encapsular" as acoes da Raquete
  public int getX() {
    return this.loc_x;
  }

  public int getY() {
    return this.loc_y;
  }

  public int getAltura() {
    return this.altura_raquete;
  }

  public int getLargura() {
    return this.largura_raquete;
  }

  public int getVelocidade() {
    return this.velocidade_raquete;
  }

  public void setVelocidade(int nova_velocidade) {
    this.velocidade_raquete = nova_velocidade;
  }

  // Retorna o centro vertical da raquete para o calculo da CPU
  public double getCentroY() {
    return this.loc_y + (this.altura_raquete / 2.0);
  }

  // Area retangular para deteccao de colisao com a bola
  public Rectangle getAreaColisao() {
    return new Rectangle(this.loc_x, this.loc_y, this.largura_raquete, this.altura_raquete);
  }

  // Reposiciona a raquete ao reiniciar a partida
  public void reiniciarPosicao(int xInicial, int yInicial) {
    this.loc_x = xInicial;
    this.loc_y = yInicial;
  }

  public void moverCima() {
    if (this.loc_y > 0) {
      this.loc_y = this.loc_y - this.velocidade_raquete;
      if (this.loc_y < 0) {
        this.loc_y = 0;
        System.out.println("Limite superior de y atingido");
      }
    }
  }

  public void moverBaixo() {
    int limite_inferior = CampoJogo.ALTURA - this.altura_raquete;
    if (this.loc_y < limite_inferior) {
      this.loc_y = this.loc_y + this.velocidade_raquete;
      if (this.loc_y > limite_inferior) {
        this.loc_y = limite_inferior;
        System.out.println("Limite inferior de y atingido");
      }
    }
  }
}
