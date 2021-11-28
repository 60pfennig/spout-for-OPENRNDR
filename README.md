# spout-for-OPENRNDR
Port of the Processing library of Spout for OPENRNDR


# Instrcutions
1) install Spout and get the Spout SDK. (https://spout.zeal.co/)
2) Add the files from this repository to your project or take this as starting point
3) In IntelliJ set run vm options:
-Djava.library.path=[PATH_TO_SPOUT_SDK_DLL_LIB_FOLDER]

4) If you have created your own project make sure you change in your build.gradle.kts:
```kotlin
runtimeOnly(openrndr("gl3"))
runtimeOnly(openrndrNatives("gl3"))
```
to
```kotlin
implementation(openrndr("gl3"))
implementation(openrndrNatives("gl3"))
```
5) You can now create your Spout sender or receiver with the correspondig classes SpoutSender and SpoutReceiver. A small example is prepared in the Main.kt
