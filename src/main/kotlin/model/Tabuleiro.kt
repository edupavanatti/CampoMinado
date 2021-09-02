package model

import java.util.Random

enum class TabuleiroEvento { VITORIA, DERROTA }

class Tabuleiro(val linhas: Int, val colunas: Int, private val minas: Int) {

    private val campos = ArrayList<ArrayList<Campo>>()
    private val callbacks = ArrayList<(TabuleiroEvento) -> Unit>()

    init {
        gerarCampos()
        associarVizinhos()
        sortearMinas()
    }

    private fun gerarCampos() {
        for (linha in 0 until linhas) {
            campos.add(ArrayList())
            for (coluna in 0 until colunas) {
                val novoCampo = Campo(linha, coluna)
                novoCampo.onEvento(this::verificarEstado)
                campos[linha].add(novoCampo)
            }
        }
    }

    private fun associarVizinhos() {
        forEachCampo { associarVizinhos(it) }
    }

    private fun associarVizinhos(campo: Campo) {
        val (linha, coluna) = campo
        val linhas = arrayOf(linha - 1, linha, linha + 1)
        val colunas = arrayOf(coluna - 1, coluna, coluna + 1)

        linhas.forEach { linhaVizinha ->
            colunas.forEach { colunaVizinha ->
                val campoAtual = campos.getOrNull(linhaVizinha)?.getOrNull(colunaVizinha)
                campoAtual?.takeIf { campo != it }?.let { campo.addVizinho(it) }
            }
        }
    }

    private fun sortearMinas() {
        val geradorAleatorio = Random()

        var linhaSorteada: Int
        var colunaSorteada: Int
        var minasAtuais = 0

        while (minasAtuais < minas) {
            linhaSorteada = geradorAleatorio.nextInt(linhas)
            colunaSorteada = geradorAleatorio.nextInt(colunas)

            val campoSorteado = campos[linhaSorteada][colunaSorteada]
            if (campoSorteado.seguro) {
                campoSorteado.minar()
                minasAtuais++
            }
        }
    }

    private fun objetivoAlcancado(): Boolean {
        var jogadorGanhou = true
        forEachCampo { if (!it.objetivoAlcancado) jogadorGanhou = false }
        return jogadorGanhou
    }

    private fun verificarEstado(evento: CampoEvento) {
        if (evento == CampoEvento.EXPLOSAO) {
            callbacks.forEach { it(TabuleiroEvento.DERROTA) }
        } else if (objetivoAlcancado()) {
            callbacks.forEach { it(TabuleiroEvento.VITORIA) }
        }
    }

    fun forEachCampo(callback: (Campo) -> Unit) {
        campos.forEach { linha -> linha.forEach(callback) }
    }

    fun onEvento(callback: (TabuleiroEvento) -> Unit) {
        callbacks.add(callback)
    }

    fun reiniciar() {
        forEachCampo { it.reiniciar() }
        sortearMinas()
    }
}