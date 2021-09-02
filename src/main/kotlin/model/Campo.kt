package model

enum class CampoEvento { ABERTURA, MARCACAO, DESMARCACAO, EXPLOSAO, REINICIALIZACAO }

data class Campo(val linha: Int, val coluna: Int) {

    private val vizinhos = ArrayList<Campo>()
    private val callbacks = ArrayList<(CampoEvento) -> Unit>()

    private var marcado: Boolean = false
    private var aberto: Boolean = false
    private var minado: Boolean = false

    private val fechado: Boolean get() = !aberto
    val seguro: Boolean get() = !minado
    val objetivoAlcancado: Boolean get() = seguro && aberto || minado && marcado
    val vizinhosMinados: Int get() = vizinhos.filter { it.minado }.size
    private val vizinhancaSegura: Boolean get() =
        vizinhos.map { it.seguro }.reduce { resultado, seguro -> resultado && seguro }

    fun addVizinho(vizinho: Campo) {
        vizinhos.add(vizinho)
    }

    fun onEvento(callback: (CampoEvento) -> Unit) {
        callbacks.add(callback)
    }

    fun abrir() {
        if (fechado && !marcado) {
            aberto = true
            if (minado) {
                callbacks.forEach { it(CampoEvento.EXPLOSAO) }
            } else {
                callbacks.forEach { it(CampoEvento.ABERTURA) }
                vizinhos.filter { it.fechado && it.seguro && vizinhancaSegura }.forEach { it.abrir() }
            }
        }
    }

    fun alterarMarcacao() {
        if (fechado) {
            marcado = !marcado
            val evento = if (marcado) CampoEvento.MARCACAO else CampoEvento.DESMARCACAO
            callbacks.forEach { it(evento) }
        }
    }

    fun minar() {
        minado = true
    }

    fun reiniciar() {
        aberto = false
        minado = false
        marcado = false
        callbacks.forEach { it(CampoEvento.REINICIALIZACAO) }
    }
}