package spout
//Kotlin JNI doesnt seem to work
object SpoutJNI {
    // Initialization - return a pointer to a spout object
    external fun init(): Long
    external fun deInit(ptr: Long)

    // Sender
    external fun createSender(name: String?, width: Int, height: Int, ptr: Long): Boolean
    external fun updateSender(name: String?, width: Int, height: Int, ptr: Long): Boolean
    external fun releaseSender(ptr: Long): Boolean
    external fun sendTexture(w: Int, h: Int, texID: Int, texTarget: Int, bInvert: Boolean, ptr: Long): Boolean

    // Receiver
    external fun setReceiverName(name: String?, ptr: Long)
    external fun createReceiver(name: String?, dim: IntArray?, ptr: Long): Boolean
    external fun releaseReceiver(ptr: Long): Boolean
    external fun receivePixels(dim: IntArray?, pix: IntArray?, ptr: Long): Boolean
    external fun receiveTexture(dim: IntArray?, texID: Int, texTarget: Int, bInvert: Boolean, ptr: Long): Boolean
    external fun senderDialog(ptr: Long): Boolean
    external fun getSenderName(ptr: Long): String?
    external fun getSenderWidth(ptr: Long): Int
    external fun getSenderHeight(ptr: Long): Int

    // Frame count
    external fun getSenderFps(ptr: Long): Float
    external fun getSenderFrame(ptr: Long): Int
    external fun isFrameNew(ptr: Long): Boolean
    external fun disableFrameCount(ptr: Long)

    // Logging
    external fun enableSpoutLog(ptr: Long)
    external fun spoutLogToFile(filename: String?, bAppend: Boolean, ptr: Long)
    external fun spoutLogLevel(level: Int, ptr: Long)
    external fun spoutLog(logtext: String?, ptr: Long)
    external fun spoutLogVerbose(logtext: String?, ptr: Long)
    external fun spoutLogNotice(logtext: String?, ptr: Long)
    external fun spoutLogWarning(logtext: String?, ptr: Long)
    external fun spoutLogError(logtext: String?, ptr: Long)
    external fun spoutLogFatal(logtext: String?, ptr: Long)

    // Common
    external fun getTextureID(ptr: Long): Int
    external fun getMemoryShareMode(ptr: Long): Boolean
    external fun getShareMode(ptr: Long): Int
    external fun setAdapter(index: Int, ptr: Long): Boolean

    // SpoutControls
    external fun createControl(
        name: String?,
        type: String?,
        minimum: Float,
        maximum: Float,
        value: Float,
        text: String?,
        ptr: Long
    ): Boolean

    external fun openControls(name: String?, ptr: Long): Boolean
    external fun checkControls(
        name: Array<String?>?,
        type: IntArray?,
        value: FloatArray?,
        text: Array<String?>?,
        ptr: Long
    ): Int

    external fun openController(path: String?, ptr: Long): Boolean
    external fun closeControls(ptr: Long): Boolean

    // Shared memory
    external fun createSenderMemory(name: String?, width: Int, height: Int, ptr: Long): Boolean
    external fun updateSenderMemorySize(name: String?, width: Int, height: Int, ptr: Long): Boolean
    external fun writeSenderString(buf: String?, ptr: Long): Boolean
    external fun closeSenderMemory(ptr: Long)
    external fun lockSenderMemory(ptr: Long): Long
    external fun unlockSenderMemory(ptr: Long)

    // Sync event signals
    external fun setFrameSync(SenderName: String?, ptr: Long)
    external fun waitFrameSync(SenderName: String?, dwTimeout: Int, ptr: Long): Boolean

    // Per-frame metadata
    external fun writeMemoryBuffer(name: String?, data: String?, length: Int, ptr: Long): Boolean
    external fun readMemoryBuffer(name: String?, maxlength: Int, ptr: Long): String?
    external fun createMemoryBuffer(name: String?, length: Int, ptr: Long): Boolean
    external fun deleteMemoryBuffer(ptr: Long): Boolean
    external fun getMemoryBufferSize(name: String?, ptr: Long): Int

    init {

        // String jvm_location = System.getProperties().getProperty("java.home") + "/" + "bin" + "/" + "java.exe";
        // System.out.println(jvm_location);
        val jvm_version = System.getProperty("java.version")

        // Java instead of operating system
        val sunDataModel = System.getProperty("sun.arch.data.model")
        println("spout.Spout " + sunDataModel + "bit v2.0.7.0 - Java " + jvm_version)
        // System.out.println("Java " + sunDataModel + "bit " + jvm_version);
        if (sunDataModel == "32") System.loadLibrary("JNISpout_32") else if (sunDataModel == "64") System.loadLibrary(
            "JNISpout_64"
        )
    }
}