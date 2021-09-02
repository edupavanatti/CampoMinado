package view

import model.Tabuleiro
import model.TabuleiroEvento
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main() {
    TelaPrincipal()
}

class TelaPrincipal : JFrame() {

    private val tabuleiro = Tabuleiro(16, 30, 90)
    private val painelTabuleiro = PainelTabuleiro(tabuleiro)

    init {
        tabuleiro.onEvento(this::mostrarResultado)
        add(painelTabuleiro)

        setSize(600, 430)
        setLocationRelativeTo(null)

        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true
    }

    private fun mostrarResultado(evento: TabuleiroEvento) {
        SwingUtilities.invokeLater {
            val mensagem = when (evento) {
                TabuleiroEvento.VITORIA -> "Você ganhou!"
                TabuleiroEvento.DERROTA -> "Você perdeu!"
            }

            JOptionPane.showMessageDialog(this, mensagem)

            tabuleiro.reiniciar()
            painelTabuleiro.repaint()
            painelTabuleiro.validate()
        }
    }
}