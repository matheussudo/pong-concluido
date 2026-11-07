import java.awt.Rectangle;
import java.util.Random;

public class Bola {

  private int loc_x, loc_y, altura_bola, largura_bola;
  private int velocidade_x, velocidade_y;
  private int velocidade_base;
  private int velocidade_maxima;
  private int rebotes_para_acelerar;
  private int contador_rebotes;
  private Random gerador = new Random();

  public Bola(int x_bola_inicial, int y_bola_inicial) {
    this.altura_bola = 15;
    this.largura_bola = 15;
    this.velocidade_base = 5;
    this.velocidade_maxima = 12;
    this.rebotes_para_acelerar = 5;
    this.contador_rebotes = 0;

    this.loc_x = x_bola_inicial;
    this.loc_y = y_bola_inicial;

    this.velocidade_x = this.velocidade_base;
    this.velocidade_y = 4;
  }

  // Getters para encapsulamento dos atributos da Bola
  public int getX() {
    return this.loc_x;
  }

  public int getY() {
    return this.loc_y;
  }

  public int getAltura() {
    return this.altura_bola;
  }

  public int getLargura() {
    return this.largura_bola;
  }

  public int getDiametro() {
    return this.largura_bola;
  }

  public int getVelocidadeX() {
    return this.velocidade_x;
  }

  public int getVelocidadeY() {
    return this.velocidade_y;
  }

  public int getVelocidadeAtual() {
    return Math.abs(this.velocidade_x);
  }

  public double getCentroY() {
    return this.loc_y + (this.altura_bola / 2.0);
  }

  // Metodos para corrigir a posicao da bola em colisoes com bordas e raquetes
  public void setX(int novo_x) {
    this.loc_x = novo_x;
  }

  public void setY(int novo_y) {
    this.loc_y = novo_y;
  }

  // Configura como a velocidade da bola vai progredir de acordo com a dificuldade
  public void configurarProgressaoVelocidade(int base, int rebotes_acelerar, int maxima) {
    this.velocidade_base = base;
    this.rebotes_para_acelerar = rebotes_acelerar;
    this.velocidade_maxima = maxima;
  }

  // Atualiza a posicao da bola somando os deslocamentos a cada ciclo do Timer
  public void mover() {
    this.loc_x = this.loc_x + this.velocidade_x;
    this.loc_y = this.loc_y + this.velocidade_y;
  }

  public void inverterVelocidadeX() {
    this.velocidade_x = -this.velocidade_x;
  }

  public void inverterVelocidadeY() {
    this.velocidade_y = -this.velocidade_y;
  }

  // Registra o rebote na raquete e aumenta a velocidade conforme a dificuldade
  public void registrarReboteEAcelerar() {
    this.contador_rebotes = this.contador_rebotes + 1;

    if (this.contador_rebotes >= this.rebotes_para_acelerar) {
      this.contador_rebotes = 0;

      int vel_atual_x = Math.abs(this.velocidade_x);
      if (vel_atual_x < this.velocidade_maxima) {
        if (this.velocidade_x > 0) {
          this.velocidade_x = this.velocidade_x + 1;
        } else {
          this.velocidade_x = this.velocidade_x - 1;
        }

        // Tambem aumenta levemente a velocidade vertical para manter o angulo dinamico
        if (Math.abs(this.velocidade_y) < this.velocidade_maxima - 2) {
          if (this.velocidade_y > 0) {
            this.velocidade_y = this.velocidade_y + 1;
          } else {
            this.velocidade_y = this.velocidade_y - 1;
          }
        }

        System.out.println("Velocidade da bola aumentou! Velocidade atual: " + Math.abs(this.velocidade_x));
      }
    }
  }

  // Area retangular para verificar intersecao com as raquetes
  public Rectangle getAreaColisao() {
    return new Rectangle(this.loc_x, this.loc_y, this.largura_bola, this.altura_bola);
  }

  // Centraliza a bola e restaura as velocidades de saque apos um ponto
  public void reiniciar(int largura_campo, int altura_campo, int direcao_horizontal) {
    this.loc_x = (largura_campo - this.largura_bola) / 2;
    this.loc_y = (altura_campo - this.altura_bola) / 2;
    this.contador_rebotes = 0;

    if (direcao_horizontal >= 0) {
      this.velocidade_x = this.velocidade_base;
    } else {
      this.velocidade_x = -this.velocidade_base;
    }

    // Define se a bola sai subindo ou descendo no saque
    if (gerador.nextBoolean()) {
      this.velocidade_y = 4;
    } else {
      this.velocidade_y = -4;
    }
  }
}
