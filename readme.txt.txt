Run vm options:
-Djava.library.path=[PATH_TO_SPOUT_SDK_DLL_LIB_FOLDER]

in build.gradle.kts:
runtimeOnly(openrndr("gl3"))
runtimeOnly(openrndrNatives("gl3"))

to

implementation(openrndr("gl3"))
implementation(openrndrNatives("gl3"))