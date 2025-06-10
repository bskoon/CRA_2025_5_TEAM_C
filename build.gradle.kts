plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    // https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
}

tasks.test {
    useJUnitPlatform()
}
