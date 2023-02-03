package com.trybe.simuladordepix;

import java.io.IOException;

/**Classe Processador de Pix.*/
public class ProcessadorDePix {

  private final Servidor servidor;

  public ProcessadorDePix(Servidor servidor) {
    this.servidor = servidor;
  }

  /**
   * Executa a operação do pix. Aqui é implementada a lógica de negócio
   * sem envolver as interações do aplicativo com a pessoa usuária.
   *
   * @param valor Valor em centavos a ser transferido.
   * @param chave Chave Pix do beneficiário da transação.
   *
   * @throws ErroDePix Erro de aplicação, caso ocorra qualquer inconformidade.
   * @throws IOException Caso aconteça algum problema relacionado à comunicação
   *                     entre o aplicativo e o servidor na nuvem.
   */
  public void executarPix(int valor, String chave) throws ErroDePix, IOException {
    
    try (Conexao conexao = servidor.abrirConexao()) {
      validarDados(valor, chave);
      String envioPix = conexao.enviarPix(valor, chave);
      realizarOperacao(envioPix);
    }
  }

  private static boolean validarDados(int valor, String chave) throws ErroDePix {
    if (valor <= 0) {
      throw new ErroValorNaoPositivo();
    }
    if (chave == null || chave.isBlank()) {
      throw new ErroChaveEmBranco();
    }
    return true;
  }

  private static boolean realizarOperacao(String envioPix) throws ErroDePix {
    if (envioPix.equals("sucesso")) {
      return true;
    } else if (envioPix.equals("saldo_insuficiente")) {
      throw new ErroSaldoInsuficiente();
    } else if (envioPix.equals("chave_pix_nao_encontrada")) {
      throw new ErroChaveNaoEncontrada();
    } else {
      throw new ErroInterno();
    }
  }

}
