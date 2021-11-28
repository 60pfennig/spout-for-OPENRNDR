package spout

import org.openrndr.draw.Drawer
import org.openrndr.draw.renderTarget
import org.openrndr.internal.gl3.ColorBufferGL3
import org.openrndr.internal.gl3.checkGLErrors

/**
 * Must be on the same graphic card as the sender
 */
class SpoutReceiver(val spout: Spout) {
    val isConnected
        get() = spout.isReceiverConnected

    init {
        spout.setLogLevel(1)
    }

    private var renderTargetForSpout = renderTarget(1, 1) {
        depthBuffer()
        colorBuffer()
    }
    private var renderTargetCb = renderTargetForSpout.colorBuffer(0) as ColorBufferGL3

    fun connectToSender(name: String) {
        //spout.isReceiverConnected
        spout.createReceiver(name)
        try {//Otherwise getting an GL_INVALID_ENUM error
            checkGLErrors()
        } catch (e: Throwable) {
            println("this is fine.") //clearing the error flag from opengl which spout produces
        }
        if (spout.isReceiverConnected) createCB(spout.senderWidth, spout.senderHeight)
    }

    private fun createCB(width: Int, height: Int) {
        renderTargetCb.destroy()
        renderTargetForSpout.destroy()
        renderTargetForSpout = renderTarget(width, height) {
            depthBuffer()
            colorBuffer()
        }
        renderTargetCb = renderTargetForSpout.colorBuffer(0) as ColorBufferGL3
    }

    fun getNewImage(): ColorBufferGL3 = spout.receiveGraphics(renderTargetCb)

    fun drawNewImage(drawer: Drawer) {

        drawer.image(getNewImage())
    }
}