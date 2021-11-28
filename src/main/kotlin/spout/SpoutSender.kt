package spout

import org.openrndr.draw.*
import org.openrndr.internal.gl3.ColorBufferGL3
import org.openrndr.internal.gl3.checkGLErrors

/**
 * Must be on the same graphic card as the receiver
 * width and height for the sender colorBuffer
 */
class SpoutSender(val spout: Spout, val width: Int, val height: Int) {

    private val renderTargetForSpout = renderTarget(width, height) {
        depthBuffer()
        colorBuffer()
    }
    private val renderTargetCb = renderTargetForSpout.colorBuffer(0) as ColorBufferGL3

    fun startSending(name: String){
        spout.createSender(name, width, height)
        try {
            checkGLErrors()
        } catch (e: Throwable) {
            println("this is fine.") //clearing the error flag from opengl which spout produces
        }
    }

    fun sendColorBuffer(cb: ColorBufferGL3){
        cb.copyTo(renderTargetCb)
        spout.sendTexture(renderTargetCb)
    }

    fun drawOnSenderColorBuffer(drawer: Drawer, drawFunction: Drawer.() -> Unit): ColorBuffer {
        drawer.withTarget(renderTargetForSpout, drawFunction)
        spout.sendTexture(renderTargetCb)
        return renderTargetCb
    }
}