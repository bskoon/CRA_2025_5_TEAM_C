plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
}

tasks.test {
    useJUnitPlatform()
}

// Shell JAR 생성 task
tasks.register<Jar>("shellJar") {
    dependsOn(tasks.compileJava)
    archiveBaseName.set("shell")
    archiveVersion.set("1.0-SNAPSHOT")
    
    from(sourceSets.main.get().output) {
        include("shell/**")
    }
    
    manifest {
        attributes(
            "Main-Class" to "shell.TestShell"
        )
    }
}

// SSD JAR 생성 task
tasks.register<Jar>("ssdJar") {
    dependsOn(tasks.compileJava)
    archiveBaseName.set("ssd")
    archiveVersion.set("1.0-SNAPSHOT")
    
    from(sourceSets.main.get().output) {
        include("ssd/**")
    }
    
    manifest {
        attributes(
            "Main-Class" to "ssd.SSD"
        )
    }
}

// 모든 JAR을 한번에 빌드하는 task
tasks.register("buildAllJars") {
    dependsOn("shellJar", "ssdJar")
    group = "build"
    description = "Build both shell and ssd JAR files"
}

// 기본 JAR task 비활성화 (선택사항)
tasks.jar {
    enabled = false
}
