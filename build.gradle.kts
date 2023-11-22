import xyz.wagyourtail.unimined.api.task.GenSourcesTask

plugins {
    id("java")
    id("xyz.wagyourtail.unimined") version "1.1.0"
    id("xyz.wagyourtail.patchbase-creator") version "1.0.0"
    `maven-publish`
}

version = if (project.hasProperty("version_snapshot")) project.properties["version"] as String + "-SNAPSHOT" else project.properties["version"] as String
group = project.properties["maven_group"] as String

base {
    archivesName.set(project.properties["archives_base_name"] as String)
}


repositories {
    mavenCentral()
}

unimined.minecraft {
    version("1.20.2")

    mappings {
        mojmap()
    }

    jarMod {
    }
}

patchbase.patchBaseCreator(sourceSets.main.get())

tasks.named<GenSourcesTask>("genSources") {
    args.add(0, "-ind=    ")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {

    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = project.properties["archives_base_name"] as String? ?: project.name
            version = project.version as String

            artifact(tasks.named("createClassPatch").get()) {
                classifier = null
            }
        }
    }
}