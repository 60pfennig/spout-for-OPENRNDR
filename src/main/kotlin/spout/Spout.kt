package spout//========================================================================================================
//
//                  spout.Spout.Java
//
//		Adds support to the functions of the JSpout JNI library.
//
//		19.12.15 - Finalised Library class
//				 - Changed all parent.println to System.out.println to prevent compiler warning
//				 - Changed "(boolean)(invertMode == 1)" to "(invertMode == 1)" to prevent compiler warning
//				 - Documented all functions
//				 - Cleanup - previous revisions in older spout.Spout.pde file
//		12.02.16 - Changed "ReceiveTexture()" to update and draw a local graphics object
//				 - Removed java.awt import - not needed for Processing 3 frame sizing
//		15.02.16 - Removed "createSender" function console output
//		26.02.16 - Updated JNISpout library dll files - tested Processing 3.0.2 64bit and 32bit
//				   spout.Spout 2.005 SDK as at 26.02.16 - incremented library version number to 2.0.5.2
//		01.03.16 - Separated initialization flag to bSenderInitialized and bReceiverConnected
//				 - Added "updateSender" to JNISpout.java and the JNI dll
//				 - Introduced createSenderName using the sketch folder name as default
//		06.03.16 - Introduced object pointers for multiple senders / receivers
//		17.03.16 - Fixed release of receiver when the received sender closed
//		18.03.16 - Fixed initial detection of named sender for CreateReceiver
//		25.03.16 - Removed "Settings" from multiple examples to work with Processing 2.2.1
//		30.03.16 - Rebuild for spout.Spout 2.005 release - version 2.0.5.3
//		28.04.16 - Added "receivePixels"
//		10.05.16 - Added SpoutControls example
//		12.05.16 - Library release - version 2.0.5.4
//		02.06.16 - Library release - version 2.0.5.5 for spout.Spout 2.005 June 2016
//		07.07.16 - Updated for latest SDK functions
//				   co.zeal.spout project removed
//		09.10.16 - Introduced cleanup function for dispose
//				   https://github.com/processing/processing/issues/4381#issuecomment-252198109
//		15.01.17 - Change to Processing 3.2.3 core library.
//				   Added getShareMode - 0 Texture, 1 CPU, 2 Memory
//		26.01.17 - Rebuild for spout.Spout 2.006 - version 2.0.6.0
//		27.01.17 - Some comment changes for CreateSender.
//				 - JNISpout - changes to OpenSpoutController to find a SpoutControls installation
//		08.02.17 - Change to Processing 3.2.4 core library.
//				 - SpoutControls example removed - duplicate of SpoutBox in the SpoutControls installation
//				 - Rebuild with current SDK files
//				 - Library release - version 2.0.6.0 for spout.Spout 2.006 - February 2017
//		10.02.12 - Changed Build properties to java.target.version=1.8
//		16.06.17 - Added getSenderName, getSenderWidth, getSenderHeight
//				 - Update JNISpout
//		18.06.17 - Change to Processing 3.2.4 core library
//				 - Update Version to 2.0.7.0 for spout.Spout 2.007
//		27.06.17 - Change to Processing 3.2.5 core library
//		17.11.18 - Updates for spout.Spout 2.007
//				   New frame option for all SDK sending and receiving functions
//				   Add getSenderFps, getSenderFrame
//				   Change application CreateReceiver with name option to setupReceiver
//				   Change receiveTexture() to void, always draw graphics
//				   Change to Processing 3.4 core library
//		18.11.18 - Change return types for receive functions
//				   Simplified functions with object arguments passed by reference
//				   https://processing.org/tutorials/objects/
//				   Add receiveGraphics and receiveImage
//				   Allocate local graphics object in constructor
//		03.01.19 - Revise examples
//		26.01.19 - Change to Processing 3.5.2 core library
//				   Add logging functions
//				   Update JNISpout libraries
//		27.01.19 - Set InfoBox message dialog topmost so it does not get lost
//		25.04.19 - Add pgr.loadPixels() before getTexture in ReceiveTexture
//				   To avoid "pixels array is null" error the first time it is accessed
//				   Minor changes to code layout in createReceiver
//		20.05.19 - Change setupReceiver to setReceiverName
//				   Change receiveTexture from void to boolean. Updated receiver examples.
//				   Add if(pgs.loaded to drawTexture
//		04.06.19   Rebuild with 2.007 SDK and JNISpout library
//		06.06.19   Rebuild for 256 max senders for 2.007
//		10.06.19   Changed Eclipse compiler compliance to Java 1.8
//		13.10.19   Rebuild for revised spout.Spout SDK
//		27.11.19   Rebuild for revised spout.Spout SDK
//		22.01.20   Update to Processing 3.5.4 core
//				   Removed setupSender function - createSender still used
//		13.02.20   Add setAdapter
//				   Rebuild for revised 2.007 spout.Spout SDK and JNISpout library
//		16.04.20   spoutCleanup : use closeSender or closeReceiver instead of release functions directly
//		19.06.20   Rebuild for GitHub update (Processing 3.5.4)
//		24.12.20   Update for SpoutGL changes
//				   Add setReceiverName to JNISpout
//				   Add setSenderName
//				   TODO : sender graphics resize
//				   Rebuild for revised 2.007 SpoutGL and updated JNISpout library (Processing 3.5.4)
//		24.01.21   Rebuild for 2.007 release Version 2.0.7.1
//		13.04.21   Add setFrameSync, waitFrameSync, writeMemoryBuffer, readMemoryBuffer
//		21.05.21   Rebuild JNI/Processing libraries
//		22.05.21   Add data sender/receiver examples
//		27.05.21   Add createMemoryBuffer
//		14.06.21   Rebuild for 2.007e release Version 2.0.7.2
//
//
// ========================================================================================================
// for infoBox
import org.openrndr.internal.gl3.ColorBufferGL3
import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.random.Random

/**
 * Main Class to use with Processing
 *
 * @author Lynn Jarvis, Martin Froehlich
 *
 * IMPORTANT: It seems that everything using the spout JNI must be in the spout package
 */
class Spout() {
    // A pointer to the spout object created
    var spoutPtr: Long

    //var parent: PApplet
    //var pgl: PGraphicsOpenGL
    //var pgs // local graphics object for receiving textures
    //   : PGraphics
    var senderName // the sender name
            : String
    var userSenderName // user specified sender name for CreateReceiver
            : String
    var dim = IntArray(2) // Sender dimensions
    var bSenderInitialized // sender initialization flag
            : Boolean

    /**
     * @return receiver status
     */
    var isReceiverConnected // receiver initialization flag
            : Boolean
    var bPixelsLoaded: Boolean
    var receiverType // 0 - parent, 1 - graphics, 2 - image
            : Int
    var invertMode // User setting for texture invert
            : Int// JNISpout.getMemoryBufferSize(spoutPtr);

  /*  private var internalColorBuffer =  ColorBufferGL3.create(1,1, levels = 1, session = Session.active, multisample = BufferMultisample.Disabled)
    set(colorBuffer: ColorBufferGL3){
        field.destroy()
        field = colorBuffer
    }*/

    /**
     * Get sender shared memory buffer size.
     *
     * @return size of memory map
     */
    var memoryBufferSize // shared memory buffer size
            = 0

    /**
     * Can be called from the sketch to release the library resources.
     * Such as from an over-ride of "exit" in the sketch for Processing 3.0.2
     */
    fun release() {
        // infoBox("spout.Spout release");
        dispose()
    }

    /**
     * This method should be called automatically when the sketch is disposed.
     * Differences observed between Processing versions :
     * 3.0.1 calls it for [X] window close or esc but not for the "Stop" button,
     * 3.0.2 does not call it at all.
     * 3.2.3 calls it for esc or [X] but not for the stop button.
     * 3.5.2 calls it for esc or [X] but not for the stop button.
     * 3.5.4 calls it for esc or [X] but not for the stop button.
     * Senders are apparently released because they can't be detected subsequently.
     */
    fun dispose() {
        // infoBox("spout.Spout dispose");
        spoutCleanup()
    }

    /**
     * The class finalizer - adapted from Syphon.
     * Never seems to be called.
     */
    @Throws(Throwable::class)
    protected fun finalize() {
        // infoBox("spout.Spout finalize");
        try {
            spoutCleanup()
        } finally {
            //super.finalize()
        }
    }
    // =========================================== //
    //                   SENDER                    //
    // =========================================== //
    /**
     * Initialize a sender name.
     * If the name is not specified, the sketch folder name is used.
     *
     * @param name : sender name
     */
    private fun changeSenderName(name: String) {
        senderName = if (name.isEmpty() || name == "") {
            "${Random.nextInt()}..${Random.nextInt()}"
        } else {
            name
        }
    }

    /**
     * Initialize a sender with the sketch window dimensions.
     * If the sender name is not specified, the sketch folder name is used.
     *
     * @param name : sender name (up to 256 characters)
     * @return true if the sender was created
     */
    fun createSender(name: String, sharedColorBuffer: ColorBufferGL3): Boolean {
        changeSenderName(name)
        return createSender(name, sharedColorBuffer.width, sharedColorBuffer.height)
    }

    /**
     * Initialize a sender.
     *
     * The name provided is registered in the list of senders
     * Initialization is made using or whatever the user has selected
     * with SpoutDXmode : Texture, CPU or Memory. Texture share only
     * succeeds if the graphic hardware is compatible, otherwise it
     * defaults to CPU texture share mode.
     *
     * @param name : sender name (up to 256 characters)
     * @param Width : sender width
     * @param Height : sender height
     * @return true if the sender was created
     */
    fun createSender(name: String, Width: Int, Height: Int): Boolean {
        changeSenderName(name)
        if (JNISpout.createSender(senderName, Width, Height, spoutPtr)) {
            bSenderInitialized = true
            dim[0] = Width
            dim[1] = Height
            println("Created sender '" + senderName + "' (" + dim[0] + "x" + dim[1] + ")")
            spoutReport(bSenderInitialized) // console report
        }
        return bSenderInitialized
    }

    /**
     * Update the size of the current sender
     *
     * @param Width : new width
     * @param Height : new height
     */
    fun updateSender(Width: Int, Height: Int) {
        if (bSenderInitialized) { // There is a sender name
            JNISpout.updateSender(senderName, Width, Height, spoutPtr)
            dim[0] = Width
            dim[1] = Height
        }
    }

    /**
     * Close the sender.
     *
     * This releases the sender name from the list of senders
     * and releases all resources for the sender.
     */
    fun closeSender() {
        if (bSenderInitialized) {
            if (JNISpout.releaseSender(spoutPtr)) println("Sender closed") else println("No sender to close")
            bSenderInitialized = false
        }
    }

    /* */
    /**
     * Write the sketch drawing surface texture to
     * an opengl/directx shared texture
     *//*
    fun sendTexture() {
        if (!bSenderInitialized) {
            // Create a sender the dimensions of the sketch window
            createSender(senderName, parent.width, parent.height)
            return
        } else if (dim[0] != parent.width || dim[1] != parent.height) {
            // Update the dimensions of the sender
            updateSender(parent.width, parent.height)
            return
        }

        // Set the invert flag to the user setting if it has been selected
        // Processing Y axis is inverted with respect to OpenGL
        // so we need to invert the texture for this function
        var bInvert = true
        if (invertMode >= 0) bInvert = invertMode == 1
        pgl.beginPGL()
        // Load the current contents of the renderer's
        // drawing surface into its texture.
        pgl.loadTexture()
        // getTexture returns the texture associated with the
        // renderer's drawing surface, making sure is updated
        // to reflect the current contents off the screen
        // (or offscreen drawing surface).
        val tex: Texture = pgl.getTexture()
        JNISpout.sendTexture(tex.glWidth, tex.glHeight, tex.glName, tex.glTarget, bInvert, spoutPtr)
        pgl.endPGL()
    }*/

    /**
     * Write the texture of a graphics object.
     *
     * @param pgr : the graphics object to be used.
     */
    fun sendTexture(image: ColorBufferGL3) {
        if (!bSenderInitialized) {
            // Create a sender the dimensions of the graphics object
            dim[0] = image.width
            dim[1] = image.height
            createSender(senderName, dim[0], dim[1])
            return
        } else if (dim[0] != image.width || dim[1] != image.height) {
            // Update the dimensions of the sender
            updateSender(image.width, image.height)
            return
        }
        var bInvert = true
        if (invertMode >= 0) bInvert = invertMode == 1
        JNISpout.sendTexture(image.width, image.height, image.texture, image.target, bInvert, spoutPtr)
    }

    /**
     * Write the texture of an image object.
     *
     * @param image : the image to be used.
     *//*
    fun sendTexture(image: ColorBufferGL3) {
        if (!bSenderInitialized) {
            // Create a sender the dimensions of the image object
            createSender(senderName, image.width, image.height)
            return
        } else if (dim[0] != image.width || dim[1] != image.height) {
            // Update the dimensions of the sender
            updateSender(image.width, image.height)
            return
        }
        var bInvert = false // default for this function
        if (invertMode >= 0) bInvert = invertMode == 1
        val tex: Texture = pgl.getTexture(image)
        JNISpout.sendTexture(tex.glWidth, tex.glHeight, tex.glName, tex.glTarget, bInvert, spoutPtr)
    }*/
    // =========================================== //
    //                   RECEIVER                  //
    // =========================================== //
    /**
     * Set a sender name to receive from
     *
     * @param name : sender name to be used
     */
    fun setReceiverName(name: String) {
        userSenderName = name
        JNISpout.setReceiverName(userSenderName, spoutPtr)
    }
    /**
     * Create a Receiver.
     *
     * If the named sender is not running or if the name is not specified,
     * the receiver will attempt to connect with the active sender.
     * If the sender is found, the name is returned and set.
     *
     * @param name : sender name to be used
     * @return true if connection with a sender succeeded
     */
    /**
     * Create a Receiver.
     *
     * Create receiver using the system sender name
     * @return true if connection with a sender succeeded
     */
    @JvmOverloads
    fun createReceiver(name: String = userSenderName): Boolean {

        // Image size values passed in are modified and passed back
        // as the size of the sender that the receiver connects to.
        var name = name
        val newname: String

        // Use the user specified sender name if any.
        // If it is empty, the receiver will connect to the active sender
        if (name.isEmpty() || name == "") {
            name = userSenderName
        }
        if (JNISpout.createReceiver(name, dim, spoutPtr)) {
            // Initialization succeeded and there was a sender running
            newname = getSpoutSenderName()
            // dim will be returned with the size of the sender it connected to
            if (newname != null && !newname.isEmpty() && newname.length > 0) {
                // Set the global sender name
                senderName = newname
                dim[0] = JNISpout.getSenderWidth(spoutPtr)
                dim[1] = JNISpout.getSenderHeight(spoutPtr)
                isReceiverConnected = true
                //spoutReport(true)
                // Once connected, the user can select new senders
                // and ReceiveTexture adapts to the change without
                // needing to call createReceiver again
                return true
            }
        }
        isReceiverConnected = false
        return false
    } // end createReceiver

    /**
     * Close a receiver.
     * All resources of the receiver are released.
     */
    fun closeReceiver() {
        if (JNISpout.releaseReceiver(spoutPtr)) println("Receiver closed") else println("No receiver to close")
        isReceiverConnected = false
    }

    fun getSenderDimensions(): SenderDimension {
        return SenderDimension(dim[0], dim[1])
    }





    /**
     * Receive a graphics texture
     *
     * @param pgr : the graphics to be used
     * @return changed graphics
     */
    fun receiveGraphics(colorBuffer: ColorBufferGL3): ColorBufferGL3 {
        var bInvert = true // default for this function
        if (invertMode >= 0) bInvert = invertMode == 1
        // If not connected keep looking
        if (!isConnected) return colorBuffer
         if (isReceiverConnected) {
            if (!JNISpout.receiveTexture(dim, colorBuffer.texture, colorBuffer.target, bInvert, spoutPtr)) {
                JNISpout.releaseReceiver(spoutPtr)
                isReceiverConnected = false
            }
        }

        // created pgr must be returned for the new size
        return colorBuffer
    }

    /**
     * Receive an image texture
     *
     * @param img : the image to be used
     * @return changed image
     */
/*    fun receiveImage(img: PImage?): PImage? {

        // If not connected keep looking
        var img: PImage? = img
        if (!isConnected) return img
        if (img == null) {
            // Create an image object if the user has not done so
            img = parent.createImage(parent.width, parent.height, PConstants.ARGB)
        } else if (dim[0] != img.width || dim[1] != img.height && dim[0] > 0 && dim[1] > 0) {
            // Adjust the image to the current sender size
            img = parent.createImage(dim[0], dim[1], PConstants.ARGB)
        } else if (isReceiverConnected) {
            // Receive into local graphics first
            pgs = receiveGraphics(pgs)
            // Use the PGraphics texture as the cache object for the image
            // Adapted from Syphon client code
            // https://github.com/Syphon/Processing/blob/master/src/codeanticode/syphon/SyphonClient.java
            val tex: Texture = pgl.getTexture(pgs)
            pgl.setCache(img, tex)
        }
        // created img must be returned for the new size
        return img
    }// If no sender, keep looking*/

    /**
     * Is the receiver connected to a sender?
     *
     * @return new frame status
     */
    val isConnected: Boolean
        get() {
            if (!isReceiverConnected) {
                // If no sender, keep looking
                if (!createReceiver()) return false
            }
            return true
        }

    /**
     * Get the current sender name
     * Checks for sender existence
     *
     * @return sender name
     */
    private fun getSpoutSenderName(): String {
        return JNISpout.getSenderName(spoutPtr) ?: ""
    }

    /**
     * Get the current sender width
     *
     * @return sender width
     */
    val senderWidth: Int
        get() = dim[0]

    /**
     * Get the current sender height
     *
     * @return sender height
     */
    val senderHeight: Int
        get() = dim[1]// Avoid integer truncation as far as possible

    /**
     * Get the current sender frame rate
     *
     * @return fps
     */
    val senderFps: Int
        get() =// Avoid integer truncation as far as possible
            Math.round(JNISpout.getSenderFps(spoutPtr) + 0.5f).toInt()

    /**
     * Get the current sender frame number
     *
     * @return frame number
     */
    val senderFrame: Int
        get() = JNISpout.getSenderFrame(spoutPtr)

    /**
     * Is the received frame new
     *
     * isFrameNew can be used after receiving a texture
     * to return whether the received frame is new.
     * It is only necessary if there is a special application for it.
     *
     * @return status
     */
    val isFrameNew: Boolean
        get() = JNISpout.isFrameNew(spoutPtr)

    /**
     * Disable frame counting for this application
     *
     */
    fun disableFrameCount() {
        JNISpout.disableFrameCount(spoutPtr)
    }

    /**
     * Pop up SpoutPanel to select a sender.
     *
     * If the user selected a different one, attach to it.
     * Requires spout.Spout installation 2.004 or higher.
     */
    fun selectSender() {
        JNISpout.senderDialog(spoutPtr)
    }

    /*
	 * Enable logging to default console output
	 */
    fun enableSpoutLog() {
        JNISpout.enableSpoutLog(spoutPtr)
    }

    /*
	 * Log to a file
	 */
    fun enableSpoutLogFile(filename: String?) {
        JNISpout.spoutLogToFile(filename, false, spoutPtr)
    }

    /*
	 * Append logs to a file
	 */
    fun enableSpoutLogFile(filename: String?, append: Boolean) {
        JNISpout.spoutLogToFile(filename, append, spoutPtr)
    }

    /*
	 * Set the spout.Spout log level
	 * @param level
	 * 0 - Disable : 1 - Verbose : 2 - Notice (default)
	 * 3 - Warning : 4 - Error   : 5 - Fatal
	 */
    fun setLogLevel(level: Int) {
        JNISpout.spoutLogLevel(level, spoutPtr)
    }

    /**
     * Log
     * @param text - log text
     */
    fun spoutLog(text: String?) {
        JNISpout.spoutLog(text, spoutPtr)
    }

    /**
     * Verbose log
     * @param text - log text
     */
    fun spoutLogVerbose(text: String?) {
        JNISpout.spoutLogVerbose(text, spoutPtr)
    }

    /**
     * Notice log
     * @param text - log text
     */
    fun spoutLogNotice(text: String?) {
        JNISpout.spoutLogNotice(text, spoutPtr)
    }

    /**
     * Warning log
     * @param text - log text
     */
    fun spoutLogWarning(text: String?) {
        JNISpout.spoutLogWarning(text, spoutPtr)
    }

    /**
     * Error log
     * @param text - log text
     */
    fun spoutLogError(text: String?) {
        JNISpout.spoutLogError(text, spoutPtr)
    }

    /**
     * Fatal log
     * @param text - log text
     */
    fun spoutLogFatal(text: String?) {
        JNISpout.spoutLogFatal(text, spoutPtr)
    }

    /**
     * Set the adapter for spout.Spout output
     * @param index
     * @return
     */
    fun setAdapter(index: Int): Boolean {
        return JNISpout.setAdapter(index, spoutPtr)
    }

    /**
     * Resize the receiver drawing surface and sketch window to that of the sender
     *
     * Requires Processing 3+
     */
    /*fun resizeFrame() {
        if (!isReceiverConnected) return
        if (parent.width !== dim[0] || parent.height !== dim[1] && dim[0] > 0 && dim[1] > 0) {
            // Only for Processing 3
            parent.getSurface().setSize(dim[0], dim[1])
        }
    }*/

    /**
     * Release everything
     */
    fun spoutCleanup() {
        // infoBox("SpoutCleanup");
        if (bSenderInitialized) closeSender()
        if (isReceiverConnected) closeReceiver()
        if (spoutPtr > 0) JNISpout.deInit(spoutPtr)
        spoutPtr = 0
    }
    // =========================================== //
    //               SPOUTCONTROLS                 //
    // =========================================== //
    /**
     * Create a control with defaults.
     *
     * @param name - control name
     * @param type - text (string), bool, event or float
     * @return true for success
     */
    fun createSpoutControl(name: String?, type: String?): Boolean {
        return JNISpout.createControl(name, type, 0f, 1f, 1f, "", spoutPtr)
    }

    /**
     * Create a control with default value.
     *
     * @param name : control name
     * @param type : text, float, bool or event
     * @param value : default value
     * @return true for success
     */
    fun createSpoutControl(name: String?, type: String?, value: Float): Boolean {
        return JNISpout.createControl(name, type, 0f, 1f, value, "", spoutPtr)
    }

    /**
     * Create a text control with default string.
     *
     * @param name : control name
     * @param type : text
     * @param text : default text
     * @return true for success
     */
    fun createSpoutControl(name: String?, type: String?, text: String?): Boolean {
        return JNISpout.createControl(name, type, 0f, 1f, 1f, text, spoutPtr)
    }

    /**
     * Create a float control with defaults.
     * Minimum, Maximum, Default
     *
     * @param name : control name
     * @param type : float
     * @param minimum : minimum value
     * @param maximum : maximum value
     * @param value : default value
     * @return true for success
     */
    fun createSpoutControl(name: String?, type: String?, minimum: Float, maximum: Float, value: Float): Boolean {
        return JNISpout.createControl(name, type, minimum, maximum, value, "", spoutPtr)
    }

    /**
     * Open SpoutControls
     *
     * A sender creates the controls and then calls OpenControls with a control name
     * so that the controller can set up a memory map and share data with the sender
     * as it changes the controls.
     * @param name : control map name (the sender name)
     * @return true for success
     */
    fun openSpoutControls(name: String?): Boolean {
        return JNISpout.openControls(name, spoutPtr)
    }

    /**
     * Check the controller for changed controls.
     *
     * The value or text string are changed depending on the control type.
     *
     * @param controlName : name of control
     * @param controlType : type : text, float, bool, event
     * @param controlValue : value
     * @param controlText : text
     * @return The number of controls. Zero if no change.
     */
    fun checkSpoutControls(
        controlName: Array<String?>?,
        controlType: IntArray?,
        controlValue: FloatArray?,
        controlText: Array<String?>?
    ): Int {
        return JNISpout.checkControls(controlName, controlType, controlValue, controlText, spoutPtr)
    }

    /**
     * Open the SpoutController executable to allow controls to be changed.
     *
     * Requires SpoutControls installation
     * or SpoutController.exe in the sketch path
     *
     * @return true if the controller was found and opened
     */
    /* fun openController(): Boolean {
         return JNISpout.openController(parent.sketchPath(), spoutPtr)
     }*/

    /**
     * Close the link with the controller.
     *
     * @return true for success
     */
    fun closeSpoutControls(): Boolean {
        return JNISpout.closeControls(spoutPtr)
    }
    // =========================================== //
    //               SHARED MEMORY                 //
    // =========================================== //
    /**
     * Create a sender memory map.
     *
     * @param name : sender name
     * @param Width : map width
     * @param Height : map height
     * @return True for success
     */
    fun createSenderMemory(name: String?, Width: Int, Height: Int): Boolean {
        return JNISpout.createSenderMemory(name, Width, Height, spoutPtr)
    }

    /**
     * Change the size of a sender memory map.
     *
     * @param name Sender name
     * @param Width : new map width
     * @param Height : new map height
     * @return True for success
     */
    fun updateSenderMemorySize(name: String?, Width: Int, Height: Int): Boolean {
        return JNISpout.updateSenderMemorySize(name, Width, Height, spoutPtr)
    }

    /**
     * Write a string to the memory map.
     *
     * The map size must be sufficient for the string.
     * @param sValue : string to be written
     * @return True for success
     */
    fun writeSenderString(sValue: String?): Boolean {
        return JNISpout.writeSenderString(sValue, spoutPtr)
    }

    /**
     * Close a sender memory map.
     */
    fun closeSenderMemory() {
        JNISpout.closeSenderMemory(spoutPtr)
    }

    /**
     * Lock a memory map for write or read access.
     *
     * @return Size of the memory map
     */
    fun lockSenderMemory(): Long {
        return JNISpout.lockSenderMemory(spoutPtr)
    }

    /**
     * Unlock a memory map after locking.
     *
     */
    fun unlockSenderMemory() {
        JNISpout.unlockSenderMemory(spoutPtr)
    }
    // =========================================== //
    //              Sync event signals
    // =========================================== //
    /**
     * Signal sync event.
     * Create a named sync event and set for test
     *
     */
    fun setFrameSync(sendername: String?) {
        JNISpout.setFrameSync(sendername, spoutPtr)
    }

    /**
     * Wait or test for named sync event.
     * Wait until the sync event is signalled or the timeout elapses.
     * Events are typically created based on the sender name and are
     * effective between a single sender/receiver pair.
     * - For testing for a signal, use a wait timeout of zero.
     * - For synchronization, use a timeout greater than the expected delay
     *
     * @return success of wait
     */
    fun waitFrameSync(sendername: String?, timeout: Int): Boolean {
        return JNISpout.waitFrameSync(sendername, timeout, spoutPtr)
    }
    // =========================================== //
    //              Per frame metadata
    // =========================================== //
    /**
     * Write a string to a sender shared memory buffer.
     * Create a shared memory map of the required size if it does not exist.
     * Subsequently the map size is fixed. To allow for varying string length
     * create shared memory of sufficient size in advance.
     * The map is closed when the sender is released.
     *
     * @return success of write
     */
    fun setSenderData(data: String): Boolean {
        // A sender name is required
        return if (!bSenderInitialized) false else JNISpout.writeMemoryBuffer(senderName, data, data.length, spoutPtr)

        // writeMemoryBuffer creates a map if not already
    }// A connected sender name is required

    // The memory map is created by the sender
    /**
     * Read sender shared memory buffer to a string.
     *
     * @return the string read
     */
    val senderData: String
        get() {
            // A connected sender name is required
            if (!isReceiverConnected) return ""

            // The memory map is created by the sender
            if (memoryBufferSize == 0) memoryBufferSize = JNISpout.getMemoryBufferSize(senderName, spoutPtr)
            return JNISpout.readMemoryBuffer(senderName, memoryBufferSize, spoutPtr) ?: ""
        }

    /**
     * Create sender shared memory buffer.
     * Create a shared memory map of the required size.
     * The map is closed when the sender is released.
     *
     * @return success of create
     */
    fun createSenderBuffer(length: Int): Boolean {
        // A sender name is required
        // but sender initialization is not
        if (senderName === "") return false
        if (JNISpout.createMemoryBuffer(senderName, length, spoutPtr)) {
            memoryBufferSize = length
            return true
        }
        return false
    }

    /**
     * Write buffer to sender shared memory.
     * Create a shared memory map of the required size if it does not exist.
     * The map is closed when the sender is released.
     *
     * @return success of write
     */
    fun writeMemoryBuffer(sendername: String?, data: String?, length: Int): Boolean {
        if (JNISpout.writeMemoryBuffer(sendername, data, length, spoutPtr)) {
            if (memoryBufferSize == 0) memoryBufferSize = JNISpout.getMemoryBufferSize(sendername, spoutPtr)
            return true
        }
        return false
    }

    /**
     * Read sender shared memory to buffer.
     * Open a sender memory map and retain the handle.
     * The map is closed when the receiver is released.
     *
     * @return number of bytes read
     */
    fun readMemoryBuffer(sendername: String?, maxlength: Int): String {
        return JNISpout.readMemoryBuffer(sendername, maxlength, spoutPtr) ?: ""
    }

    /**
     * Create sender shared memory buffer.
     * Create a shared memory map of the required size.
     * The map is closed when the sender is released.
     *
     * @return success of create
     */
    fun createMemoryBuffer(sendername: String?, length: Int): Boolean {
        if (JNISpout.createMemoryBuffer(sendername, length, spoutPtr)) {
            memoryBufferSize = length
            return true
        }
        return false
    }

    /**
     * Delete sender shared memory buffer.
     *
     * @return success of delete
     */
    fun deleteMemoryBuffer(): Boolean {
        if (JNISpout.deleteMemoryBuffer(spoutPtr)) {
            memoryBufferSize = 0
            return true
        }
        return false
    }
    // =========================================== //
    //                   UTILITY                   //
    // =========================================== //
    /**
     * User option to set texture inversion for send and receive
     *
     * @param bInvert : true or false as required
     */
    fun setInvert(bInvert: Boolean) {
        // invertMode is -1 unless the user specifically selects it
        invertMode = if (bInvert) 1 else 0
    }

    /**
     * Print current settings to the console.
     *
     * @param bInit : the initialization mode
     */
     fun spoutReport(bInit: Boolean) {
         var ShareMode = 0 // Texture share default
         if (bInit) {
             ShareMode = JNISpout.getShareMode(spoutPtr)
             if (ShareMode == 2) println("spout.Spout initialized memory sharing") else if (ShareMode == 1) println("spout.Spout initialized CPU texture sharing") else println(
                 "spout.Spout initialized texture sharing"
             )
         } else {
             println("spout.Spout intialization failed")
         }
     }

    /**
     * Pop up a MessageBox dialog
     *
     * @param infoMessage : the message to show
     */
    fun infoBox(infoMessage: String?) {
        // JOptionPane.showMessageDialog(null, infoMessage, "spout.Spout", JOptionPane.INFORMATION_MESSAGE);
        // Set message dialog topmost so it does not get lost
        val jf = JFrame()
        jf.isAlwaysOnTop = true
        JOptionPane.showMessageDialog(jf, infoMessage, "spout.Spout", JOptionPane.INFORMATION_MESSAGE)
    }

    /**
     * Create a spout.Spout Object.
     *
     * A spout object is created within the JNI dll
     * and a pointer to it saved in this class for
     * use with all functions.
     *
     * @param parent : parent sketch
     */
    init {

        // A pointer to the new spout object for this instance
        spoutPtr = JNISpout.init()
        if (spoutPtr == 0L) println("spout.Spout initialization failed")
        dim[0] = 0 // Sender width
        dim[1] = 0 // Sender height
        bSenderInitialized = false
        isReceiverConnected = false
        bPixelsLoaded = false
        senderName = ""
        userSenderName = ""
        receiverType = 0 // receive to parent graphics
        invertMode = -1 // User has not set any mode - use function defaults
        // Initialize a local graphics object in advance for receiveTexture
    }


} // end class spout.Spout

data class SenderDimension(val widht: Int, val height: Int)
