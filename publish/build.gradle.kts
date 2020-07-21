plugins {
    `java-base`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

group = "org.jetbrains.skija"
version = (project.findProperty("deploy.version") as? String)
        ?: "0.0.0-local"

val jar by tasks.creating(Jar::class) {
    from(zipTree(targetJar(classifier = null))) {
        excludeNatives()
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(zipTree(targetJar(classifier = "sources"))) {
        excludeNatives()
    }
}

val linuxJar by tasks.creating(Jar::class) {
    archiveClassifier.set("natives-linux-x64")
    from(targetFile("native/libskija.so"))
}

val macOsJar by tasks.creating(Jar::class) {
    archiveClassifier.set("natives-mac-x64")
    from(targetFile("native/libskija.dylib"))
}

val publicationName = "SkijaPublication"

publishing {
    repositories {
        maven {
            url = uri("${rootProject.buildDir}/repo")
        }
    }
    publications {
        create<MavenPublication>(publicationName) {
            artifactId = "skija"

            artifact(jar)
            artifact(sourcesJar)
            artifact(macOsJar)
            artifact(linuxJar)
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications(publicationName)

    pkg.apply {
        repo = "maven"
        name = "skija"
        vcsUrl = "https://github.com/JetBrains/Skija"
        issueTrackerUrl = "https://github.com/JetBrains/skija/issues"
        setLicenses("Apache-2.0")
    }
}

// Utils

fun CopySpec.excludeNatives() {
    exclude("**/*.so")
    exclude("**/*.dylib")
    exclude("**/*.dll")
}

val checkFilesExist: Boolean
    get() = project.property("check.files.exist") == "true"

fun targetFile(targetPath: String): File =
    project.file("../target").resolve(targetPath).also {
        if (checkFilesExist) {
            check(it.exists()) { "File does not exist: $it" }
        }
    }

fun targetJar(classifier: String?): File {
    val fileName = arrayOf("skija", version, classifier)
            .filterNotNull()
            .joinToString("-")
    return targetFile("$fileName.jar")
}