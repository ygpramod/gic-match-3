val jupiterVersion = "6.0.3"

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}