/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
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

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

allprojects {
    tasks.withType<Test> {
        maxParallelForks = 4
    }

    tasks.withType<Javadoc> {
        println("in tasks.withType<Javadoc>")
        options.encoding = "UTF-8"
        isFailOnError = false
        options {
        (this as StandardJavadocDocletOptions).apply {
            addBooleanOption("html5", true)
            //stylesheetFile(File("${rootDir}/src/main/javadoc/assertj-javadoc.css"))
            addBooleanOption("-allow-script-in-comments", true)
            header("<script src=\"http://cdn.jsdelivr.net/highlight.js/8.6/highlight.min.js\"></script>")
            footer("<script type=\"text/javascript\">hljs.initHighlightingOnLoad();</script>")
            tags("apiNote:a:API Note:", "implSpec:a:Implementation Requirements:", "implNote:a:Implementation Note:", "sneaky:a:Sneaky Throws:")
        }
    }
    }

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.json:json:20211205")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
    implementation("org.testng:testng:7.5")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")
}

tasks.named<Test>("test") {
    useTestNG()
}

tasks.register<Javadoc>("generateJavadoc") {
    println("Generating JavaDocs")
    source(sourceSets["main"].allJava)
}
