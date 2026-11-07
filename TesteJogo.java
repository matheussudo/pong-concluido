import java.awt.Rectangle;

public class TesteJogo {

  private static int total_testes = 0;
  private static int testes_aprovados = 0;
  private static int testes_falhos = 0;

  public static void main(String[] args) {
    System.out.println("====================================================");
    System.out.println("      INICIANDO SUITE DE TESTES - PONG EM JAVA      ");
    System.out.println("====================================================\n");

    testarRaqueteMovimentoELimites();
    testarBolaMovimentoECentralizacao();
    testarColisaoRectangle();
    testarPlacarEncapsulamento();
    testarProgressaoVelocidadeDificuldades();

    System.out.println("\n====================================================");
    System.out.println("RESUMO DOS TESTES:");
    System.out.println("Total executados : " + total_testes);
    System.out.println("Aprovados        : " + testes_aprovados);
    System.out.println("Falhos           : " + testes_falhos);
    System.out.println("====================================================");

    if (testes_falhos > 0) {
      System.exit(1);
    }
  }

  // Metodo auxiliar para validar cada condicao de teste
  private static void verificar(String nome_teste, boolean condicao) {
    total_testes = total_testes + 1;
    if (condicao) {
      testes_aprovados = testes_aprovados + 1;
      System.out.println("[OK] " + nome_teste);
    } else {
      testes_falhos = testes_falhos + 1;
      System.out.println("[FALHA] " + nome_teste);
    }
  }

  // Fase 02 e 03: Testa posicao, dimensoes, movimentacao e limites da Raquete
  private static void testarRaqueteMovimentoELimites() {
    System.out.println("--- 1. Testes da Classe Raquete ---");
    Raquete raquete = new Raquete(20, 260);

    verificar("Posicao inicial X e Y da raquete", raquete.getX() == 20 && raquete.getY() == 260);

    int y_antes = raquete.getY();
    raquete.moverCima();
    verificar("moverCima diminui o valor de Y", raquete.getY() < y_antes);

    y_antes = raquete.getY();
    raquete.moverBaixo();
    verificar("moverBaixo aumenta o valor de Y", raquete.getY() > y_antes);

    // Forcando subida ate o topo (limite 0)
    for (int i = 0; i < 200; i++) {
      raquete.moverCima();
    }
    verificar("Limite superior impede Y menor que 0", raquete.getY() == 0);

    // Forcando descida ate a base (ALTURA - altura_raquete = 520)
    for (int i = 0; i < 200; i++) {
      raquete.moverBaixo();
    }
    int limite_esperado = CampoJogo.ALTURA - raquete.getAltura();
    verificar("Limite inferior impede ultrapassar a base do campo", raquete.getY() == limite_esperado);
  }

  // Fase 04 e 06: Testa movimento, inversao de direcao e centralizacao da Bola
  private static void testarBolaMovimentoECentralizacao() {
    System.out.println("\n--- 2. Testes da Classe Bola ---");
    Bola bola = new Bola(100, 100);

    int x_inicial = bola.getX();
    int y_inicial = bola.getY();
    bola.mover();
    verificar("Metodo mover desloca X e Y conforme velocidades",
        bola.getX() != x_inicial && bola.getY() != y_inicial);

    int vel_x_antes = bola.getVelocidadeX();
    bola.inverterVelocidadeX();
    verificar("inverterVelocidadeX inverte o sinal horizontal", bola.getVelocidadeX() == -vel_x_antes);

    int vel_y_antes = bola.getVelocidadeY();
    bola.inverterVelocidadeY();
    verificar("inverterVelocidadeY inverte o sinal vertical", bola.getVelocidadeY() == -vel_y_antes);

    bola.reiniciar(CampoJogo.LARGURA, CampoJogo.ALTURA, 1);
    int centro_x_esperado = (CampoJogo.LARGURA - bola.getDiametro()) / 2;
    int centro_y_esperado = (CampoJogo.ALTURA - bola.getDiametro()) / 2;
    verificar("reiniciar centraliza a bola corretamente no campo",
        bola.getX() == centro_x_esperado && bola.getY() == centro_y_esperado);
  }

  // Fase 05: Testa representacao e intersecao das areas de colisao (Rectangle)
  private static void testarColisaoRectangle() {
    System.out.println("\n--- 3. Testes de Colisao (Rectangle) ---");
    Raquete raquete = new Raquete(20, 250);
    Bola bola_colidindo = new Bola(22, 270);
    Bola bola_distante = new Bola(400, 300);

    Rectangle area_raquete = raquete.getAreaColisao();
    verificar("Detecta intersecao quando a bola toca na raquete",
        bola_colidindo.getAreaColisao().intersects(area_raquete));
    verificar("Nao detecta intersecao quando a bola esta longe da raquete",
        !bola_distante.getAreaColisao().intersects(area_raquete));
  }

  // Fase 06 e 08: Testa marcacao de pontos e reinicio do Placar
  private static void testarPlacarEncapsulamento() {
    System.out.println("\n--- 4. Testes da Classe Placar ---");
    Placar placar = new Placar();

    verificar("Placar inicia em 0 a 0",
        placar.getPontosJogador() == 0 && placar.getPontosCpu() == 0);

    placar.marcarPontoJogador();
    placar.marcarPontoJogador();
    placar.marcarPontoCpu();
    verificar("Contabiliza pontos corretamente para Jogador e CPU",
        placar.getPontosJogador() == 2 && placar.getPontosCpu() == 1);

    placar.zerarPlacar();
    verificar("zerarPlacar restaura pontuacao para 0 a 0",
        placar.getPontosJogador() == 0 && placar.getPontosCpu() == 0);
  }

  // Fase 07 e Desafio: Testa aumento de velocidade nos niveis Facil, Medio e Dificil
  private static void testarProgressaoVelocidadeDificuldades() {
    System.out.println("\n--- 5. Testes de Dificuldade e Aumento de Velocidade ---");
    Bola bola = new Bola(400, 300);

    // Nivel Facil: base 4, acelera a cada 6 rebotes
    bola.configurarProgressaoVelocidade(4, 6, 10);
    bola.reiniciar(CampoJogo.LARGURA, CampoJogo.ALTURA, 1);
    for (int i = 0; i < 5; i++) {
      bola.registrarReboteEAcelerar();
    }
    verificar("Nivel Facil mantem velocidade antes de completar 6 rebotes",
        bola.getVelocidadeAtual() == 4);
    bola.registrarReboteEAcelerar(); // 6o rebote
    verificar("Nivel Facil aumenta velocidade ao completar 6 rebotes",
        bola.getVelocidadeAtual() == 5);

    // Nivel Medio: base 5, acelera a cada 3 rebotes
    bola.configurarProgressaoVelocidade(5, 3, 12);
    bola.reiniciar(CampoJogo.LARGURA, CampoJogo.ALTURA, 1);
    bola.registrarReboteEAcelerar();
    bola.registrarReboteEAcelerar();
    bola.registrarReboteEAcelerar(); // 3o rebote
    verificar("Nivel Medio aumenta velocidade a cada 3 rebotes",
        bola.getVelocidadeAtual() == 6);

    // Nivel Dificil: base 6, acelera a cada 1 rebote
    bola.configurarProgressaoVelocidade(6, 1, 15);
    bola.reiniciar(CampoJogo.LARGURA, CampoJogo.ALTURA, 1);
    bola.registrarReboteEAcelerar(); // 1o rebote
    bola.registrarReboteEAcelerar(); // 2o rebote
    verificar("Nivel Dificil aumenta velocidade imediatamente a cada rebote",
        bola.getVelocidadeAtual() == 8);

    // Verifica se o saque restaura a velocidade inicial
    bola.reiniciar(CampoJogo.LARGURA, CampoJogo.ALTURA, -1);
    verificar("Reinicio apos ponto restaura velocidade base de saque",
        bola.getVelocidadeAtual() == 6);
  }
}
