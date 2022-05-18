/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
}

application {
    // Define the main class for this application
    mainClass.set("com.lyit.csd.app.Main")
}

sourceSets {
  main {
    java {
    }
  }
}

group = "groupId"
version = "1.0-SNAPSHOT"
description = "Portfolio"

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.json:json:20220320")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")

    testImplementation("org.testng:testng:7.6.0")
    testImplementation("org.slf4j:slf4j-api:1.7.36")
    testImplementation("org.slf4j:slf4j-simple:1.7.36")
}

tasks.named<Test>("test") {
    println("in tasks.named<Test>(test)")
    testLogging.showStandardStreams = true
    testLogging.events("passed", "failed", "skipped")
    useTestNG() {
        useDefaultListeners = true
        suites("testng-unittests.xml")
    }
}

tasks.withType<Javadoc> {
    println("in tasks.withType<Javadoc>")
    options.encoding = "UTF-8"
    isFailOnError = false
    options {
        (this as StandardJavadocDocletOptions).apply {
            addBooleanOption("html5", true)
            addBooleanOption("-allow-script-in-comments", true)
            header("<script src=\"http://cdn.jsdelivr.net/highlight.js/8.6/highlight.min.js\"></script>")
            footer("<script type=\"text/javascript\">hljs.initHighlightingOnLoad();</script>")
        }
    }
}

tasks.register<Javadoc>("generateJavadoc") {
    println("tasks.register<Javadoc>(generateJavadoc)")
    dependsOn("build")
    println("Generating JavaDocs")
    source(sourceSets["main"].allJava)
}