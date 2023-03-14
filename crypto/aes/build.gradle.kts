plugins {
    id("smallraw.android.library")
    id("smallraw.android.library.native")
}

android {
    namespace = "com.smallraw.crypto"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material.material)

    testImplementation(project(":crypto:core-java"))
}

tasks {
    val osxDir = projectDir.absolutePath + "/.externalNativeBuild/cmake/debug/osx/"

    register("printSystemEnv") {
        val folder = File(osxDir)
        doLast {
            println("""
                <========== System Env ==========>
                JAVA_HOME :${System.getenv("JAVA_HOME")}
                """.trimIndent())
        }
    }

    register("createBuildDir") {
        dependsOn("printSystemEnv")
        val folder = File(osxDir)
        doLast {
            if (!folder.exists()) {
                folder.mkdirs()
            }
        }
    }

    register("runCMake", Exec::class) {
        dependsOn("createBuildDir")
        workingDir = File(osxDir) // Jump to future build directory
        commandLine("/opt/homebrew/bin/cmake")
        // commandLine("/usr/local/bin/cmake") // Path from HomeBrew installation
        args("../../../../") // Relative path for out-of-source builds
    }

    register("runMake", Exec::class) {
        dependsOn("runCMake")
        workingDir = File(osxDir)
        commandLine("make")
    }
}

project.afterEvaluate {
    // Not sure how much of a hack this is - but it allows CMake/SWIG to run before Android Studio
    // complains about missing generated files
    // TODO: Probably need a release hook too?
    try {
        tasks.getByName("javaPreCompileDebug")
            .dependsOn(tasks.getByName("externalNativeBuildDebug"))
        if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            tasks.getByName("testDebugUnitTest").dependsOn(tasks.getByName("runMake"))
        }
    } catch (e: UnknownTaskException) {
        println("not find javaPreCompileDebug Task")
    }
}
