import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import spout.Spout
import spout.SpoutReceiver
import spout.SpoutSender


fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        val font = loadFont("data/fonts/default.otf", 24.0)
        val spoutName = "demoSender"
        val spout = Spout()
        val spoutSender = SpoutSender(spout, width, height)
        spoutSender.startSending(spoutName)

        val spoutReceiver = SpoutReceiver(spout)
        keyboard.keyDown.listen {
            if (it.name == "c")
                spoutReceiver.connectToSender("sds")
        }
        extend {
            //If we connected (in this case to our own) spout sender we draw the received image
            if (spoutReceiver.isConnected)
                spoutReceiver.drawNewImage(drawer)
            //while we are not connected we draw this as default
            else {
                drawer.fill = ColorRGBa.WHITE
                drawer.fontMap = font
                drawer.text("Press c to connect to the sender", 20.0, height / 2.0)
            }
            //Drawing this for every receiver who wants our art
            spoutSender.drawOnSenderColorBuffer(drawer) {
                clear(ColorRGBa.PINK)
                fill = ColorRGBa.BLACK
                fontMap = font
                text("Spout connection established! :)", 20.0, height / 2.0)
            }
        }
    }
}
