// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradleVersion}")
        classpath(kotlin("gradle-plugin", version = Versions.kotlinVersion))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.google.com")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}