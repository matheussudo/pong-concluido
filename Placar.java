public class Placar {

  private int pontos_player;
  private int pontos_cpu;

  public Placar() {
    this.pontos_player = 0;
    this.pontos_cpu = 0;
  }

  // Metodos de consulta dos pontos
  public int getPontosJogador() {
    return this.pontos_player;
  }

  public int getPontosCpu() {
    return this.pontos_cpu;
  }

  // Metodos para contabilizar pontos mantendo o encapsulamento
  public void marcarPontoJogador() {
    this.pontos_player = this.pontos_player + 1;
    System.out.println("Ponto do Jogador! Placar: " + this.pontos_player + " x " + this.pontos_cpu);
  }

  public void marcarPontoCpu() {
    this.pontos_cpu = this.pontos_cpu + 1;
    System.out.println("Ponto da CPU! Placar: " + this.pontos_player + " x " + this.pontos_cpu);
  }

  // Zera o placar para iniciar uma nova partida
  public void zerarPlacar() {
    this.pontos_player = 0;
    this.pontos_cpu = 0;
  }
}
