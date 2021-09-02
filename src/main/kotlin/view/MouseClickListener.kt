package view

import model.Campo
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class MouseClickListener(
    private val campo: Campo,
    private val onLeftButtonClicked: (Campo) -> Unit,
    private val onRightButtonClicked: (Campo) -> Unit
) : MouseListener {

    override fun mousePressed(event: MouseEvent?) {
        when (event?.button) {
            MouseEvent.BUTTON1 -> onLeftButtonClicked(campo)
            MouseEvent.BUTTON3 -> onRightButtonClicked(campo)
        }
    }

    override fun mouseClicked(event: MouseEvent?) {}
    override fun mouseReleased(event: MouseEvent?) {}
    override fun mouseEntered(event: MouseEvent?) {}
    override fun mouseExited(event: MouseEvent?) {}
}