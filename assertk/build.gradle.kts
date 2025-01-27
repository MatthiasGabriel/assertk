plugins {
    id("assertk.multiplatform")
    id("assertk.publish")
}

val compileTemplates by tasks.registering(TemplateTask::class) {
    inputDir.set(file("src/template"))
    outputDir.set(file("$buildDir/generated/template"))
}

val compileTestTemplates by tasks.registering(TemplateTask::class) {
    inputDir.set(file("src/testTemplate"))
    outputDir.set(file("$buildDir/generated/testTemplate"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvm {
        withJava()
    }

    targets.all {
        compilations.findByName("test")?.kotlinOptions {
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.opentest4k)
            }
            kotlin.srcDir(compileTemplates)
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlin.coroutines)
            }
            kotlin.srcDir(compileTestTemplates)
        }
        jvmMain {
            dependencies {
                compileOnly(kotlin("reflect"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(kotlin("test-junit"))
            }
        }
        jsTest {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}